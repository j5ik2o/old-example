package example.db

import java.time.ZonedDateTime

import akka.NotUsed
import akka.stream.scaladsl.Source

import scala.concurrent.ExecutionContext

class TodoDas(todoDao: TodoDao) {

  def findById(id: TodoId): Source[Todo, NotUsed] =
    Source.single(id).flatMapConcat { id => Source.fromPublisher(todoDao.resolveAsStream(id)) }

  def findAll: Source[Todo, NotUsed] =
    Source.fromPublisher(todoDao.resolveAllAsStream)

  def create(todo: Todo)(implicit ec: ExecutionContext): Source[Unit, NotUsed] =
    Source.single(todo).mapAsync(1) { todo =>
      todoDao.create(todo)
    }

  def update(id: TodoId, text: String, updateAt: ZonedDateTime)(implicit ec: ExecutionContext): Source[Unit, NotUsed] =
    Source.single((id, text, updateAt)).mapAsync(1) {
      case (id, text, updateAt) =>
        todoDao.update(id, text, updateAt)
    }

  def delete(id: TodoId)(implicit ec: ExecutionContext): Source[Unit, NotUsed] =
    Source.single(id).mapAsync(1) { id =>
      todoDao.delete(id)
    }

}
