package example.domain

import java.time.ZonedDateTime

case class Todo(id: TodoId, text: String, createAt: ZonedDateTime, updateAt: ZonedDateTime)
