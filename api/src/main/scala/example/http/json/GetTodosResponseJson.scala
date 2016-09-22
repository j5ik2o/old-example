package example.http.json

import spray.json.DefaultJsonProtocol

case class GetTodosResponseJson(values: Seq[GetTodoResponseJson])

object GetTodosResponseJsonProtocol extends DefaultJsonProtocol {
  import GetTodoResponseJsonProtocol._
  implicit val getTodosResponseJsonFormat = jsonFormat1(GetTodosResponseJson.apply)

}

