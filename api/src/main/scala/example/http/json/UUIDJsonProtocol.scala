package example.http.json

import java.util.UUID

import spray.json.{ DefaultJsonProtocol, JsString, JsValue, JsonFormat }
import spray.json._

object UUIDJsonProtocol extends DefaultJsonProtocol {

  implicit object UUIDJsonFormat extends JsonFormat[UUID] {

    override def read(json: JsValue): UUID = json match {
      case JsString(value) =>
        try {
          UUID.fromString(value)
        } catch {
          case ex: IllegalArgumentException => error(value)
        }
      case _ =>
        error(json.toString())
    }

    override def write(obj: UUID): JsValue =
      JsString(obj.toString)

    def error(v: Any): UUID = {
      val example = UUID.randomUUID()
      deserializationError(f"'$v' is not a valid UUID value., e.g. '$example'")
    }

  }

}
