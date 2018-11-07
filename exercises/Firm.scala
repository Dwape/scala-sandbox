package exercises

case class Employee(name: String, sinceYear: Int, dependents: List[Employee] = Nil) {
  def subTree: List[Employee] = this :: dependents.flatMap(_.subTree)

  def moreThat2Years: Boolean = {
    val list = this.dependents.sortBy(_.sinceYear)
    list.drop(1).zip(list).exists { case (a, b) => a.sinceYear > 2 + b.sinceYear }
  }
}

object Firm {

  def numberOfEmployees(president: Employee): Int = {
    president.subTree.length
  }

  def bosses(president: Employee): List[Employee] = {
    president
      .subTree
      .filter(_.dependents.nonEmpty)
  }

  def moreThat2Years(president: Employee): List[Employee] = {
    bosses(president).filter(_.moreThat2Years)
  }
}

object FirmApp extends App {

  val employee1 = Employee("Chad", 1975)
  val employee2 = Employee("Tom", 1972)
  val president = Employee("Dude", 1970 , List(employee1, employee2))

  //println(Firm.numberOfEmployees(president))
  //println(Firm.bosses(president))
  println(Firm.moreThat2Years(president))
}