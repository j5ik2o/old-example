package example.http.json

import java.time.ZonedDateTime
import java.util.UUID

import spray.json.DefaultJsonProtocol

case class GetTodoResponseJson(id: UUID, text: String, createAt: ZonedDateTime, updateAt: ZonedDateTime)

object GetTodoResponseJsonProtocol extends DefaultJsonProtocol {

  import UUIDJsonProtocol._
  import ZonedDateTimeJsonProtocol._

  implicit val getTodoResponseJsonFormat = jsonFormat4(GetTodoResponseJson.apply)

}