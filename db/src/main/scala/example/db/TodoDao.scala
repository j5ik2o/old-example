package example.db

import java.time.ZonedDateTime

import org.slf4j.LoggerFactory
import slick.backend.DatabasePublisher

import scala.concurrent.{ ExecutionContext, Future }

/**
 * [Todo]用DAO。
 *
 * @param pp [ProfileProvider]
 */
class TodoDao(pp: ProfileProvider) {

  import pp._
  import pp.profile.api._

  private val logger = LoggerFactory.getLogger(classOf[TodoDao])

  /**
   * [Todo]を作成する。
   *
   * @param todo [TODO]
   * @param ec [ExecutionContext]
   * @return Unit of Future
   */
  def create(todo: Todo)(implicit ec: ExecutionContext): Future[Unit] = {
    db.run {
      logger.debug(s"create $todo")
      Todos += todo
    }.map { result =>
      logger.debug("result = " + result)
      if (result == 0)
        throw new Exception()
      else
        ()
    }
  }

  /**
   * [Todo]を更新する。
   *
   * @param id 識別子
   * @param text TODOの内容
   * @param updateAt 更新日時
   * @param ec [ExecutionContext]
   * @return Unit of Future
   */
  def update(id: TodoId, text: String, updateAt: ZonedDateTime, version: Long)(implicit ec: ExecutionContext): Future[Unit] = {
    db.run {
      logger.debug(s"update $id, $text, $updateAt, $version")
      Todos
        .filter(_.id === id)
        .map(t => (t.text, t.updateAt, t.version))
        .update(text, updateAt, version)
    }.map { result =>
      logger.debug("result = " + result)
      if (result == 0)
        throw new Exception()
      else
        ()
    }
  }

  /**
   * TODOを削除する。
   *
   * @param id 識別子
   * @param ec [ExecutionContext]
   * @return Unit of Future
   */
  def delete(id: TodoId)(implicit ec: ExecutionContext): Future[Unit] = {
    db.run {
      logger.debug(s"delete $id")
      Todos
        .filter(_.id === id)
        .delete
    }.map { result =>
      logger.debug("result = " + result)
      if (result == 0)
        throw new Exception()
      else
        ()
    }
  }

  /**
   * 識別子に対応するTODOを取得する(ストリーム版)。
   *
   * @param id 識別子
   * @return [Todo] of DatabasePublisher
   */
  def resolveAsStream(id: TodoId): DatabasePublisher[Todo] = {
    db.stream {
      Todos.findById(id).result
    }
  }

  /**
   * 識別子に対応するTODOを取得する(Future版)。
   *
   * @param id 識別子
   * @return [Todo] of Future
   */
  def resolve(id: TodoId)(implicit ec: ExecutionContext): Future[Option[Todo]] = {
    db.run {
      Todos.findById(id).result.headOption
    }
  }

  /**
   * 全件のTODOを取得する(ストリーム版)
   *
   * @return All [Todo] of DatabasePublisher
   */
  def resolveAllAsStream: DatabasePublisher[Todo] = {
    db.stream {
      Todos.result
    }
  }

  /**
   * 全件のTODOを取得する(Future版)
   *
   * @return All [Todo] of Future
   */
  def resolveAll: Future[Seq[Todo]] = {
    db.run {
      Todos.result
    }
  }

}
