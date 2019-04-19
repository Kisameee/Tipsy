package giveaways

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import giveaways.FakeData._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object WebServer {

  // needed to run the route
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // formats for unmarshalling and marshalling
  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat3(Item)
  implicit val orderFormat: RootJsonFormat[Order] = jsonFormat1(Order)

  def main(args: Array[String]) {

    val route: Route =
      pathPrefix("tips") {
        get {
          path("listedonors" / LongNumber) { id =>
            // there might be no item for a given id
            val maybeItem: Option[Item] = fetchItembById(id)
            maybeItem match {
              case Some(item) => complete(item)
              case None => complete(StatusCodes.NotFound)
            }
          }
        }
        post {
          path("makeTip") {

          }
        }
        post {
          path("cancelTip") {

          }
        }
        get {
          path("sumAllTips") {

          }
        }
        get {
          path("getAllTipsByUser" / LongNumber) {

          }
        }
      }
    pathPrefix("sub"/ LongNumber){ id =>
      // there might be no item for a given id
      val maybeItem: Option[Item] = fetchItembById(id)
      maybeItem match {
        case Some(item) => complete(item)
        case None
        => complete(StatusCodes.NotFound)
      }
    }
    pathPrefix("giveaways") {
      post {
        path("createGiveAway" / LongNumber) {

        }
      }
      post {
        path ("RegisterToGiveAway" / LongNumber) {

        }
      }
      get {
        path("getWinner") {

        }
      }
    }
    pathPrefix("survey") {
      post("createSurvey" / Survey) {

      }
      post ("doSurvey") {

      }
      get("getSurveyResult" / LongNumber) {

      }
    }
    get {
      pathPrefix("item" / LongNumber) { id =>
        // there might be no item for a given id
        val maybeItem: Option[Item] = fetchItembById(id)
        maybeItem match {
          case Some(item) => complete(item)
          case None
          => complete(StatusCodes.NotFound)
        }
      }
    } ~
      post {
        path("create-order") {
          entity(as[Order]) { order =>
            val saved: Done = saveOrder(order)
            saved match {
              case Done => complete("order created")
              case _ => complete(StatusCodes.NotFound)
            }
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done

  }
}