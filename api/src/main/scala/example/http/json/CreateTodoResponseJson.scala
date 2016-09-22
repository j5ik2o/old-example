package example.http.json

import java.util.UUID

import spray.json.DefaultJsonProtocol

case class CreateTodoResponseJson(id: UUID, aggregateId: UUID)

object CreateTodoResponseJsonProtocol extends DefaultJsonProtocol {

  import UUIDJsonProtocol._

  implicit val createTodoResponseJSONFormat = jsonFormat2(CreateTodoResponseJson.apply)

}

