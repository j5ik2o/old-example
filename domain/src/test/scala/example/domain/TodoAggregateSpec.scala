package example.domain

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit }
import org.scalatest.{ BeforeAndAfterAll, FunSpecLike }
import scala.concurrent.duration._

class TodoAggregateSpec
  extends TestKit(ActorSystem("TodoAggregateSpec"))
    with FunSpecLike
    with BeforeAndAfterAll
    with ImplicitSender {

  import TodoProtocol._

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  describe("TodoAggregate") {
    it("should be able to create a todo") {
      val todoId = TodoId(UUID.randomUUID())
      val text = "text"
      val todoAggregateRef = system.actorOf(TodoAggregate.props(todoId))
      todoAggregateRef ! Create(UUID.randomUUID(), todoId, text)
      val response = expectMsgType[CreateSuccess](10 seconds)
      assert(response.text == text)
    }
    it("should be able to update a todo") {
      val todoId = TodoId(UUID.randomUUID())
      val text = "text"
      val todoAggregateRef = system.actorOf(TodoAggregate.props(todoId))
      todoAggregateRef ! Create(UUID.randomUUID(), todoId, text)
      val response1 = expectMsgType[CreateSuccess](10 seconds)
      assert(response1.text == text)
      todoAggregateRef ! Update(UUID.randomUUID(), todoId, text)
      val response2 = expectMsgType[UpdateSuccess](10 seconds)
      assert(response2.text == text)
    }
    it("should be able to delete a todo") {
      val todoId = TodoId(UUID.randomUUID())
      val text = "text"
      val todoAggregateRef = system.actorOf(TodoAggregate.props(todoId))
      todoAggregateRef ! Create(UUID.randomUUID(), todoId, text)
      val response1 = expectMsgType[CreateSuccess](10 seconds)
      assert(response1.text == text)
      todoAggregateRef ! Delete(UUID.randomUUID(), todoId)
      expectMsgType[DeleteSuccess](10 seconds)
    }
    it("should be able to get a todo") {
      val todoId = TodoId(UUID.randomUUID())
      val text = "text"
      val todoAggregateRef = system.actorOf(TodoAggregate.props(todoId))
      todoAggregateRef ! Create(UUID.randomUUID(), todoId, text)
      val response1 = expectMsgType[CreateSuccess](10 seconds)
      assert(response1.text == text)
      todoAggregateRef ! Get(UUID.randomUUID(), todoId)
      val response2 = expectMsgType[GetSuccess](10 seconds)
      assert(response2.todo.text == text)
    }
  }

}
