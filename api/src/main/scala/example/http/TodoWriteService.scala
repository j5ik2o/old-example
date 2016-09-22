package example.http

import java.util.UUID

import akka.actor.ActorRef
import akka.pattern.ask
import akka.http.scaladsl.server.{ Directives, Route }
import example.domain.TodoId
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.util.Timeout
import example.http.json._

import scala.concurrent.duration._

trait TodoWriteService extends Directives {

  import example.domain.TodoProtocol._
  import example.http.json.CreateTodoRequestJsonProtocol._
  import example.http.json.CreateTodoResponseJsonProtocol._
  import example.http.json.UpdateTodoRequestJsonProtocol._
  import example.http.json.UpdateTodoResponseJsonProtocol._
  import example.http.json.DeleteTodoRequestJsonProtocol._
  import example.http.json.DeleteTodoResponseJsonProtocol._

  implicit val timeout = Timeout(10 seconds)

  protected val todoAggregateRef: ActorRef

  val todoWriteRoute: Route = createTodo ~ updateTodo ~ deleteTodo

  private val createTodo: Route = {
    path("todos") {
      post {
        entity(as[CreateTodoRequestJson]) { requestBody =>
          val future = (todoAggregateRef ? Create(UUID.randomUUID(), TodoId(UUID.randomUUID()), requestBody.text)).mapTo[CreateSuccess]
          onSuccess(future) { res =>
            complete(CreateTodoResponseJson(res.id, res.aggregateId.value))
          }
        }
      }
    }
  }

  private val updateTodo: Route = {
    path("todos") {
      put {
        entity(as[UpdateTodoRequestJson]) { requestBody =>
          val future = (todoAggregateRef ? Update(UUID.randomUUID(), TodoId(requestBody.aggregateId), requestBody.text)).mapTo[UpdateSuccess]
          onSuccess(future) { res =>
            complete(UpdateTodoResponseJson(res.id, res.aggregateId.value))
          }
        }
      }
    }
  }

  private val deleteTodo: Route = {
    path("todos") {
      delete {
        entity(as[DeleteTodoRequestJson]) { requestBody =>
          val future = (todoAggregateRef ? Delete(UUID.randomUUID(), TodoId(requestBody.aggregateId))).mapTo[DeleteSuccess]
          onSuccess(future) { res =>
            complete(DeleteTodoResponseJson(res.id, res.aggregateId.value))
          }
        }
      }
    }
  }

}
