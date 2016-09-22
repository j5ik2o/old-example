package example.db

import java.sql.Timestamp
import java.time.{ Instant, ZoneId, ZonedDateTime }
import java.util.UUID

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

case class ProfileProvider(configName: String) extends Tables {

  lazy val dc = DatabaseConfig.forConfig[JdbcProfile](configName)
  lazy val db = dc.db

  override def getProfile: JdbcProfile = dc.driver

}

trait Tables {

  def getProfile: JdbcProfile

  lazy val profile: JdbcProfile = getProfile

  import profile.api._

  implicit val uuidToString = MappedColumnType.base[TodoId, String](
    { case TodoId(l) => l.toString },
    d => TodoId(UUID.fromString(d))
  )

  implicit val zdtToTs = MappedColumnType.base[ZonedDateTime, java.sql.Timestamp](
    l => new Timestamp(l.toInstant.toEpochMilli),
    d => ZonedDateTime.ofInstant(Instant.ofEpochMilli(d.getTime), ZoneId.systemDefault())
  )

  class Todos(tag: Tag) extends Table[Todo](tag, "TODO") {

    def id = column[TodoId]("ID", O.PrimaryKey, O.Length(128, varying = true))

    def text = column[String]("TEXT", O.Length(256, varying = true))

    def createAt = column[ZonedDateTime]("CREATE_AT")

    def updateAt = column[ZonedDateTime]("UPDATE_AT")

    def version = column[Long]("VERSION")

    def * = (id, text, createAt, updateAt, version) <> (Todo.tupled, Todo.unapply)

  }

  object Todos extends TableQuery(new Todos(_)) {
    def findById = this.findBy(_.id)
  }

}
