package example.http.json

import java.time.format.DateTimeFormatter
import java.time.{ ZoneId, ZonedDateTime }

import spray.json.{ DefaultJsonProtocol, JsString, JsValue, JsonFormat, _ }

object ZonedDateTimeJsonProtocol extends DefaultJsonProtocol {

  implicit object ZonedDateTimeJsonFormat extends JsonFormat[ZonedDateTime] {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault)

    override def write(obj: ZonedDateTime): JsValue =
      JsString(formatter.format(obj))

    override def read(json: JsValue): ZonedDateTime = json match {
      case JsString(s) => try {
        ZonedDateTime.parse(s, formatter)
      } catch {
        case t: Throwable => error(s)
      }
      case _ =>
        error(json.toString())
    }

    def error(v: Any): ZonedDateTime = {
      val example = formatter.format(ZonedDateTime.now())
      deserializationError(f"'$v' is not a valid date value. Dates must be in compact ISO-8601 format, e.g. '$example'")
    }

  }

}
