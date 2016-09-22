package example.domain

import java.time.ZonedDateTime
import java.util.UUID

object TodoProtocol {

  sealed trait Request {
    val id: UUID
    val aggregateId: TodoId
  }

  sealed trait Response {
    val id: UUID
    val requestId: UUID
    val aggregateId: TodoId
  }

  sealed trait Event {
    val id: UUID
    val aggregateId: TodoId
  }
  // ---

  case class Get(id: UUID, aggregateId: TodoId) extends Request

  sealed trait GetResponse extends Response

  case class GetSuccess(id: UUID, requestId: UUID, aggregateId: TodoId, todo: Todo) extends GetResponse

  case class GetError(id: UUID, requestId: UUID, aggregateId: TodoId, ex: Exception) extends GetResponse

  // ---

  case class Create(id: UUID, aggregateId: TodoId, text: String) extends Request

  sealed trait CreateResponse extends Response

  case class CreateSuccess(
    id:          UUID,
    requestId:   UUID,
    aggregateId: TodoId,
    text:        String,
    createAt:    ZonedDateTime,
    updateAt:    ZonedDateTime
  )
      extends CreateResponse

  case class CreateError(
    id:          UUID,
    requestId:   UUID,
    aggregateId: TodoId,
    ex:          Exception
  )
      extends CreateResponse

  case class Created(
    id:          UUID,
    aggregateId: TodoId,
    text:        String,
    createAt:    ZonedDateTime,
    updateAt:    ZonedDateTime
  )
      extends Event

  // ---

  sealed trait UpdateOrDeleteRequest extends Request

  case class Update(id: UUID, aggregateId: TodoId, text: String) extends UpdateOrDeleteRequest

  sealed trait UpdateResponse extends Response

  case class UpdateSuccess(
    id:          UUID,
    requestId:   UUID,
    aggregateId: TodoId,
    text:        String,
    updateAt:    ZonedDateTime
  )
      extends UpdateResponse

  case class UpdateError(
    id:          UUID,
    requestId:   UUID,
    aggregateId: TodoId,
    ex:          Exception
  )
      extends UpdateResponse

  case class Updated(
    id:          UUID,
    aggregateId: TodoId,
    text:        String,
    updateAt:    ZonedDateTime
  )
      extends Event

  // ---

  case class Delete(id: UUID, aggregateId: TodoId) extends UpdateOrDeleteRequest

  sealed trait DeleteResponse extends Response

  case class DeleteSuccess(
    id:          UUID,
    requestId:   UUID,
    aggregateId: TodoId,
    updateAt:    ZonedDateTime
  )
      extends DeleteResponse

  case class DeleteError(
    id:          UUID,
    requestId:   UUID,
    aggregateId: TodoId,
    ex:          Exception
  )
      extends DeleteResponse

  case class Deleted(
    id:          UUID,
    aggregateId: TodoId,
    updateAt:    ZonedDateTime
  )
      extends Event
}
