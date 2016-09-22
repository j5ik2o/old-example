package example.readModelUpdater

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.query.PersistenceQuery
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.scaladsl.Source

class ReadModelUpdater(implicit val system: ActorSystem) {

  private val readJournal: LeveldbReadJournal =
    PersistenceQuery(system)
      .readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)

  val persistenceIdsSource: Source[String, NotUsed] = readJournal.allPersistenceIds()

  val getPersistenceWithModelNameFlow = persistenceIdsSource.map{ id =>
    val modelName = id.split("-").head
    (id, modelName)
  }




}
