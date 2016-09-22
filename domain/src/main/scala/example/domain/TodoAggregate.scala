package example.domain

import java.time.ZonedDateTime
import java.util.UUID

import akka.actor.{ ActorRef, Props }
import akka.persistence.{ PersistentActor, SnapshotOffer }

object TodoAggregate {

  val typeName = "todo"

  def name(id: TodoId): String = s"${typeName}-${id.value}"

  def props(id: TodoId, subscriber: Option[ActorRef] = None): Props =
    Props(new TodoAggregate(id, subscriber))

}

private class TodoAggregate(id: TodoId, subscriber: Option[ActorRef]) extends PersistentActor {
  import TodoProtocol._

  private var state: Option[Todo] = None

  private var eventCounter = 0L

  override def persistenceId: String = TodoAggregate.name(id)

  override def receiveRecover: Receive = {
    case Created(_, aggregateId, text, createAt, updateAt) =>
      require(aggregateId == id)
      state = Some(Todo(aggregateId, text, createAt, updateAt))
    case Updated(_, aggregateId, text, updateAt) =>
      require(aggregateId == id)
      state.map(_.copy(text = text, updateAt = updateAt))
    case Deleted(_, aggregateId, updateAt) =>
      require(aggregateId == id)
      state = None
      context.stop(self)
    case SnapshotOffer(metadata, snapshot: Todo) =>
      state = Some(snapshot)
  }

  private def saveSnapshotEntity() = {
    if (eventCounter % 10 == 0) {
      saveSnapshot(state.get)
    }
    eventCounter += 1
  }

  override def receiveCommand: Receive = {
    case Create(requestId, aggregateId, text) =>
      require(aggregateId == id)
      val now = ZonedDateTime.now()
      state = Some(Todo(id, text, now, now))
      persist(Created(UUID.randomUUID(), aggregateId, text, now, now)) { event =>
        sender() ! CreateSuccess(UUID.randomUUID(), requestId, aggregateId, text, now, now)
        subscriber.foreach(_ ! event)
        saveSnapshotEntity()
      }
    case Update(requestId, aggregateId, text) =>
      require(aggregateId == id)
      val now = ZonedDateTime.now()
      state.map(_.copy(text = text, updateAt = now))
      persist(Updated(UUID.randomUUID(), aggregateId, text, now)) { event =>
        sender() ! UpdateSuccess(UUID.randomUUID(), requestId, aggregateId, text, now)
        subscriber.foreach(_ ! event)
        saveSnapshotEntity()
      }
    case Delete(requestId, aggregateId) =>
      require(aggregateId == id)
      val now = ZonedDateTime.now()
      state = None
      persist(Deleted(UUID.randomUUID(), aggregateId, now)) { event =>
        sender() ! DeleteSuccess(UUID.randomUUID(), requestId, aggregateId, now)
        subscriber.foreach(_ ! event)
        saveSnapshotEntity()
        context.stop(self)
      }
    case Get(requestId, aggregateId) =>
      require(aggregateId == id)
      sender() ! GetSuccess(UUID.randomUUID(), requestId, aggregateId, state.get)
  }

}
