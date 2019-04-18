package giveaways

import akka.Done

object FakeData {

  final case class Item(name: String, id: Long, tips: Long)

  final case class Order(items: List[Item])

  var orders: List[Item] = List(Item("item1", 1, 10), Item("item2", 2, 20))


  def fetchItem(itemId: Long): Option[Item] = orders.find(o => o.id == itemId)

  def saveOrder(order: Order): Done = {
    orders = order match {
      case Order(items) => items ::: orders
      case _ => orders
    }
    Done
  }
}