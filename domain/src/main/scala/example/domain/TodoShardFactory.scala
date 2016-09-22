package example.domain

import akka.actor.Props
import akka.cluster.sharding.ShardRegion.{ ExtractEntityId, ExtractShardId }

case class TodoShardFactory(protected val props: Props) extends ClusterShardingSupport {
  import TodoProtocol._

  override val typeName: String = TodoAggregate.typeName

  override val extractEntityId: ExtractEntityId = {
    case req: Request =>
      (entryKey(req.aggregateId.value.toString), req)
  }

  override val extractShardId: ExtractShardId = {
    case req: Request =>
      shardKey(req.aggregateId.value.toString)
  }

}
