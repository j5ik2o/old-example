package example.http.json

import spray.json.DefaultJsonProtocol

case class CreateTodoRequestJson(text: String)

object CreateTodoRequestJsonProtocol extends DefaultJsonProtocol {

  implicit val createTodoJSONFormat = jsonFormat1(CreateTodoRequestJson.apply)

}

