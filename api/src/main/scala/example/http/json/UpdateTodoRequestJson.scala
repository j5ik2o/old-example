package example.http.json

import java.util.UUID

import spray.json.DefaultJsonProtocol

case class UpdateTodoRequestJson(aggregateId: UUID, text: String)

object UpdateTodoRequestJsonProtocol extends DefaultJsonProtocol {

  import UUIDJsonProtocol._

  implicit val updateTodoRequestJsonFormat = jsonFormat2(UpdateTodoRequestJson.apply)

}
