package example.http

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import example.db.{ ProfileProvider, TodoDas, TodoDao }
import example.domain.{ TodoAggregate, TodoSupervisor }

object Main extends App with TodoWriteService with TodoReadService {

  private implicit val system = ActorSystem("akka-ddd-sample")

  override protected implicit val materializer = ActorMaterializer()

  private implicit val executionContext = system.dispatcher

  private val profileProvider = ProfileProvider("datasource")

  private val todoDao = new TodoDao(profileProvider)

  override protected val todoDas = new TodoDas(todoDao)

  override protected val todoAggregateRef: ActorRef =
    system.actorOf(TodoSupervisor.props(TodoAggregate.props))

  private val route: Route = todoWriteRoute ~ todoReadRoute

  private val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  sys.addShutdownHook {
    bindingFuture
      .map(_.unbind)
      .onComplete(_ => system.terminate())
  }

}
