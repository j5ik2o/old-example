package example.http.json

import java.util.UUID

import spray.json.DefaultJsonProtocol

case class DeleteTodoResponseJson(id: UUID, aggregateId: UUID)

object DeleteTodoResponseJsonProtocol extends DefaultJsonProtocol {

  import UUIDJsonProtocol._

  implicit val deleteTodoResponseJsonFormat = jsonFormat2(DeleteTodoResponseJson.apply)

}
