package example.http.json

import java.util.UUID

import spray.json.DefaultJsonProtocol

case class UpdateTodoResponseJson(id: UUID, aggregateId: UUID)

object UpdateTodoResponseJsonProtocol extends DefaultJsonProtocol {

  import UUIDJsonProtocol._

  implicit val updateTodoResponseJsonFormat = jsonFormat2(UpdateTodoResponseJson.apply)

}
