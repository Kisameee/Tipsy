package giveaways

import akka.Done

object FakeData {

  final case class Item(name: String, id: Long, tips: Int)

  final case class Order(items: List[Item])

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

  def fetchTotalTips(): Int =
    if (FakeData.orders.isEmpty) {
      0
    } else {
      FakeData.orders.map(_.tips).sum
    }

  def saveOrder(order: Order): Done = {
    orders = order match {
      case Order(items) => items ::: orders
      case _ => orders
    }
    Done
  }
}