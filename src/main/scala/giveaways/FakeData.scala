package giveaways

import akka.Done

object FakeData {

  final case class Item(name: String, id: Long, amount: Double)
  final case class Order(items: List[Item])
  final case class User(id: Int, name: String, subscribed: Boolean, blocked: Boolean)
  final case class Survey(id: Int, question: String, option1: String, option2: String, vote1: Int, vote2: Int, participants: List[User])
  final case class Tip(id: Int, name: String, amount: Double)
  final case class GiveAway(id: Int, event: String, amount: Double, participants: List[User])

  /**********************DATA********************/

  var users: List[User] = List(
    User(1, "jesus", true, true),
    User(2, "jawad", false, false),
    User(3, "lvmh", true, false),
    User(4, "jeankevin", true, false),
    User(5, "jeanmouloud", false, true),

  )

  var tips: List[Tip] = List(
    Tip(1, "jesus", 10),
    Tip(2, "jawad", 1),
    Tip(3, "Sarah", 3),
    Tip(4, "Jean", 5),
    Tip(5, "Coralie", 20)
  )
  var giveaways: List[GiveAway] = List(
    GiveAway(1, "Event1", 1000, List(
      User(2, "jesus", false, false),
      User(3, "jawad", false, false),
      User(4, "lvmh", false, false),
      User(5, "jeankevin", false, false),
      User(6, "jeanmouloud", false, false),
    )),
    GiveAway(2, "Event4", 1000, List[User]())
  )
  var surveys: List[Survey] = List(
    Survey(
      1,
      "Q1",
      "OPT1",
      "OPT2",
      10,
      20,
      List(
        User(7, "Ahmed", false, false),
        User(8, "Beatrice", false, false),
        User(9, "Sarah", false, false)
      )),
    Survey(
      2,
      "Q2",
      "OPT1",
      "OPT2",
      10,
      20,
      List(
        User(3, "Benoit", false, false),
        User(8, "Beatrice", false, false),
        User(9, "Sarah", false, false)
      )),
    Survey(
      3,
      "Q3",
      "OPT1",
      "OPT2",
      10,
      20,
      List(
        User(1, "Jean", false, false),
        User(2, "Sebastien", false, false),
        User(9, "Sarah", false, false)
      ))
  )

  var idCount = 4
  var orders: List[Item] = List(Item("Dieu", 1, 10), Item("Jesus", 2, 20), Item("michelJackson", 3, 30), Item("jawad", 3, 300))
  private var blackListed: Set[String] = Set()
  private var followers: Set[String] = Set()


  def fetchItembById(itemId: Long): Option[Item] = orders.find(o => o.id == itemId)

  def fetchItembByName(itemName: String): Option[Item] = orders.find(o => o.name == itemName)

  def saveItem(item: Item): Done = {
    orders = item match {
      case i: Item => List(i) ::: orders
      case _ => orders
    }
    Done
  }






  /********************Tips******************/

  def tip(name: String, amount: Double): List[Tip] = {
    if (users.exists(_.name == name))
      tips :+ Tip(tips.length + 1, name, amount)
    else
      tips
  }

  def deleteTip(id: Int): Unit = {
    tips= tips.filter(_.id!=id)
  }

  def tipSum(): Option[Double] = {
    Some(tips.map(_.amount).sum)
  }

  def getTipByUser(name: String):Double = {
    tips.filter(_.name==name).map(_.amount).sum
  }

  def listOfDonators(): List[String] = {
    tips.map(_.name).distinct
  }

  def totalTips(): Option[Double] = {
    Some(tips.map(_.amount).sum)
  }

  /**********************/





  def saveOrder(order: Order): Done = {
    orders = order match {
      case Order(items) => items ::: orders
      case _ => orders
    }
    Done
  }


  /*

 */

  /* USERS */
  def blockUser(id: Int): Unit = {
    users.filter(_.id == id)
  }

  /* SURVEYS */
  def newSurvey(question: String, option1: String, option2: String): Unit = {
    surveys :+ Survey(surveys.length + 1, question, option1, option2, 0, 0, List())
  }

  def participateToSurvey(user: User, id: Int): Unit = {
    surveys.filter(_.id == id).asInstanceOf[Survey].participants :+ user
  }

  def getSurveyResult(id: Int): Any = {
    surveys.filter(_.id == id)
  }
}