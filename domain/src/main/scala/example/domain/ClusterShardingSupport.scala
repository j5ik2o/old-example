package example.domain

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.cluster.sharding.{ ClusterSharding, ClusterShardingSettings, ShardRegion }

trait ClusterShardingSupport {

  val typeName: String

  protected val props: Props

  val numOfShards = 8

  val entriesPerShard = 8

  protected def shardKey(id: String) = Math.abs(id.hashCode % numOfShards).toString

  protected def entryKey(id: String) = Math.abs(id.hashCode % (numOfShards * entriesPerShard)).toString

  val extractEntityId: ShardRegion.ExtractEntityId

  val extractShardId: ShardRegion.ExtractShardId

  def resolve(implicit system: ActorSystem): ActorRef =
    ClusterSharding(system).shardRegion(typeName)

  def start(implicit system: ActorSystem): ActorRef = {
    ClusterSharding(system).start(
      typeName,
      props,
      ClusterShardingSettings(system),
      extractEntityId,
      extractShardId
    )
  }

}
