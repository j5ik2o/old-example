package example.http.json

import java.util.UUID

import spray.json.DefaultJsonProtocol

case class DeleteTodoRequestJson(aggregateId: UUID)

object DeleteTodoRequestJsonProtocol extends DefaultJsonProtocol {

  import UUIDJsonProtocol._

  implicit val deleteTodoRequestJsonFormat = jsonFormat1(DeleteTodoRequestJson.apply)

}
