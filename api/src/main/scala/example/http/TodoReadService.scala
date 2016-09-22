package example.http

import akka.http.scaladsl.server._
import akka.stream.scaladsl.Sink
import example.db.{ TodoDas, TodoId }
import example.http.json.{ GetTodoResponseJson, GetTodosResponseJson }
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.Materializer

trait TodoReadService extends Directives {

  import example.http.json.GetTodoResponseJsonProtocol._
  import example.http.json.GetTodosResponseJsonProtocol._

  protected implicit val materializer: Materializer

  protected val todoDas: TodoDas

  protected val todoReadRoute: Route = getTodoById ~ getTodos

  private val getTodoById: Route = {
    pathPrefix("todos" / JavaUUID) { id =>
      get {
        val future = todoDas.findById(TodoId(id)).runWith(Sink.head)
        onSuccess(future) { res =>
          complete(GetTodoResponseJson(res.id.value, res.text, res.createAt, res.updateAt))
        }
      }
    }
  }

  private val getTodos: Route = {
    path("todos") {
      get {
        val future = todoDas.findAll.map { e => GetTodoResponseJson(e.id.value, e.text, e.createAt, e.updateAt) } runWith (Sink.seq)
        onSuccess(future) { res =>
          complete(GetTodosResponseJson(res))
        }
      }
    }
  }

}
