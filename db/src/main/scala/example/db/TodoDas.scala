package example.db

import java.time.ZonedDateTime

import akka.NotUsed
import akka.stream.scaladsl.Source

import scala.concurrent.ExecutionContext

class TodoDas(todoDao: TodoDao) {

  def findById(id: TodoId): Source[Todo, NotUsed] =
    Source.fromPublisher(todoDao.resolveAsStream(id))

  def findAll: Source[Todo, NotUsed] =
    Source.fromPublisher(todoDao.resolveAllAsStream)

  def create(todo: Todo)
            (implicit ec: ExecutionContext): Source[Unit, NotUsed] =
    Source.fromFuture {
      todoDao.create(todo)
    }

  def update(id: TodoId, text: String, updateAt: ZonedDateTime, version: Long)
            (implicit ec: ExecutionContext): Source[Unit, NotUsed] =
    Source.fromFuture {
      todoDao.update(id, text, updateAt, version)
    }

  def delete(id: TodoId)
            (implicit ec: ExecutionContext): Source[Unit, NotUsed] =
    Source.single(id).mapAsync(1) { id =>
      todoDao.delete(id)
    }

}
