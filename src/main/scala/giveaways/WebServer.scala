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
            complete(sum.toString())
          }
        }~
        get {
          path("getAllTipsByUser" / IntNumber) { id =>
            val tip: Double = getTipByUser(id)
            complete(tip.toString)
          }
        }
      }~
    pathPrefix("sub"){
      val subs : List[String] = getSubs()
      complete(subs)
    }~
    pathPrefix("giveaways") {
      post {
        path("createGiveAway" ) {
          parameter('id.as[Int], 'event.as[String], 'cashPrize.as[Double]) {(id, event, cashPrize) =>
            newGiveaway(id, event, cashPrize)
            complete("Donation créée !")
          }
        }
      }~
      post {
        path ("RegisterToGiveAway" ) {
          parameter('id.as[Int], 'ga_id.as[Int]) {(id, ga_id) =>
            subToGiveaway(id, ga_id)
            complete("Bien inscrit a la donation !")
          }
        }
      }~
      get {
        path("getWinner" / IntNumber) { id =>
          val win: User = getWinner(id)
          complete("Le vainqueur est :" + win.name)
        }
      }
    }~
    pathPrefix("blacklist" / IntNumber) { id =>
      blockUser(id)
      complete("L'utilisateur " + id.toString() + " a bien été ban !")
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
          complete("")
        }
        //val result

        complete("getSurveyResult")
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