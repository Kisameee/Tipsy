package giveaways

object FakeData {

  final case class Item(name: String, id: Long, amount: Double)

  final case class Order(items: List[Item])

  final case class User(id: Int, name: String, subscribed: Boolean, blocked: Boolean)

  final case class Survey(id: Int, question: String, option1: String, option2: String, vote1: Int, vote2: Int, participants: List[User])

  final case class Tip(id: Int, name: String, amount: Double)

  final case class GiveAway(id: Int, event: String, amount: Double, participants: List[User])

  /** ********************DATA ********************/

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
    Tip(3, "lvmh", 3),
    Tip(4, "jeankevin", 5),
    Tip(5, "jeanmouloud", 20)
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
        User(3, "Ahmed", false, false),
        User(5, "Beatrice", false, false),
        User(1, "Sarah", false, false)
      )),
    Survey(
      2,
      "Q2",
      "OPT1",
      "OPT2",
      10,
      20,
      List(
        User(2, "jawad", false, false),
        User(4, "lvmh", false, false),
      ))
  )

  /** ********************************************/


  /** ******************Tips ******************/

  def tip(name: String, amount: Double): List[Tip] = {
    if (users.contains(name))
      tips :+ Tip(tips.length + 1, name, amount)
    else
      tips
  }

  def deleteTip(id: Int): Unit = {
    tips = tips.filter(_.id != id)
  }

  def tipSum(): Double = {
    tips.map(_.amount).sum
  }

  def getTipByUser(id: Int): Double = {
    tips.filter(_.id == id).map(_.amount).sum
  }

  def listOfDonators(name: String, id: Int): String = {
    tips.map(_.name).distinct(id)
  }

  def totalTips(): Double = {
    tips.map(_.amount).sum
  }

  /** *********************SUBS *********************/

  def getSubs() = {
    users.filter(_.subscribed).map(_.name)
  }

  /** *****************GiveAways ********************/

  def newGiveaway(id: Int, event: String, cashPrize: Double, participants: List[User] = List[User]()): List[GiveAway] = {
    giveaways :+ GiveAway(id, event, cashPrize, participants)
  }

  def removeGiveaway(id: Int): Unit = {
    giveaways = giveaways.filter(_.id != id)
  }

  def subToGiveaway(id: Int, userId: Int) = {
    giveaways.find(_.id == id) match {
      case Some(g) =>
        val ga = g
        users.find(_.id == userId) match {
          case Some(uu) => val l = g.participants ++ List()
            giveaways = newGiveaway(ga.id, ga.event, ga.amount, l)
            giveaways
          case _ => giveaways
        }
      case _ => giveaways
    }
  }

  def getWinner(id: Int): User = {
    val r = scala.util.Random
    val list = giveaways.filter(_.id == id)(0)
    list.participants(r.nextInt(list.participants.length))
  }


  /** *********************Orders *****************/


  /** **********************Users *******************/
  def blockUser(blocked: Boolean): Any = {
    users.filter(_.blocked == true)
  }

  /** ********************Surveys ******************/

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