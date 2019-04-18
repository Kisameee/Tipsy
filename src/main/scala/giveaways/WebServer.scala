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
            val maybeItem: List[String] = listOfDonators()
            complete(maybeItem)
          }
        } ~
        post {
          path("makeTip") {
            parameters('id.as[String],  'amount.as[Int]) {(id, amount) =>
              tips = tip(id, amount)
              complete("Tip bien reçu.")
            }
          }
        } ~
        post {
          path("cancelTip" / IntNumber) { id =>
            deleteTip(id)
            complete("Tip annulé!")
          }
        }~
        get {
          path("sumAllTips") {
            val sum: Double = tipSum()
          }
        }~
        get {
          path("getAllTipsByUser" / Segment) { id =>
            val tip: Double = getTipByUser(id)
            complete(tip.toString)
          }
        }
      }~
    pathPrefix("sub"/ LongNumber){ id =>
      // there might be no item for a given id
      val maybeItem: Option[Item] = fetchItembById(id)
      maybeItem match {
        case Some(item) => complete(item)
        case None
        => complete(StatusCodes.NotFound)
      }
    }~
    pathPrefix("giveaways") {
      post {
        path("createGiveAway" ) {
          complete("createGiveAway")
        }
      }~
      post {
        path ("RegisterToGiveAway" ) {
          complete("RegisterToGiveAway")
        }
      }~
      get {
        path("getWinner") {
          complete("getWinner")
        }
      }
    }~
    pathPrefix("survey") {
      post {
        path("createSurvey") {
          parameter('question.as[String],  'option1.as[String], 'option2.as[String]) { (question, option1, option2) =>
            newSurvey(question, option1, option2)
            complete("Sondage créé !")
          }
        }
      }~
      post {
        path("doSurvey") {
          parameter('id_user.as[Int], 'id_survey.as[Int]) { (user, id) =>
            participateToSurvey(user, id)
            complete("Participation prise en compte !")
          }
        }
      }
      get {
        path("getSurveyResult" / IntNumber) { id =>
          val result: Any = participateToSurvey(id)
        }
        //val result

        complete("getSurveyResult")
      }
    }~
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