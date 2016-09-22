package example.domain

import akka.actor.{ Actor, ActorRef, Props }

object TodoSupervisor {

  def props(propsFactory: (TodoId, Option[ActorRef]) => Props): Props =
    Props(new TodoSupervisor(propsFactory))

}

private class TodoSupervisor(propsFactory: (TodoId, Option[ActorRef]) => Props) extends Actor {

  import TodoProtocol._

  override def receive: Receive = {
    case msg: Request =>
      context.child(TodoAggregate.name(msg.aggregateId)).fold(context.actorOf(propsFactory(msg.aggregateId, None)) forward msg) {
        _ forward msg
      }
  }

}
