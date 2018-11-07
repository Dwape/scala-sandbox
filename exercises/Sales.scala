package exercises

case class Sales(date: String, amounts: List[Double])

object Accountant {

  def salesGreaterThan(sales: List[Sales], amount: Double): List[String] = {
    sales.filter(a => a.amounts.sum > amount).map(_.date)
  }

  def doubled(sales: List[Sales]): List[String] = {
    val list = sales.sortBy(_.date)
    list
      .drop(1)
      .zip(list)
      .filter{case (a, b) => a.amounts.length == 2*b.amounts.length}
      .map(_._1.date)
  }

  def bestSales(sales: List[Sales], days: Int): List[String] = {
    val list = sales.sortBy(_.date)
    list
      .sliding(days) // get an iterator with lists of days consecutive dates
      .toList // turn the iterator into a list
      .maxBy{_.map(a => a.amounts.sum).sum} // order by the sum of the total amount of all days in a sublist
      .map(_.date) // return the dates
  }
}


object SalesApp extends App {

  val all = List(
    Sales("2015-05-05", List(1200, 233)),
    Sales("2015-05-06", List(3400, 24, 43)),
    Sales("2015-05-07", List(120, 340, 90, 85, 1340, 500))
  )

  //println(Accountant.salesGreaterThan(all, 1000))
  //println(Accountant.doubled(all))
  //println(Accountant.bestSales(all, 2))
}
