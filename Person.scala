case class Person(name: String, age: Int, children: List[Person] = Nil) {
  def descendants: List[Person] = children ++ children.flatMap(_.descendants)
  def grandchildren: List[Person] = children.flatMap(_.children)
}

object Inquirer {
  def minorsAdults(person: Person): (List[Person], List[Person]) = person.descendants.partition(_.age < 18)

  //Should the function be applied only on the people on the list or on its descendants as well?
  def childless(people: List[Person]): List[Person] = people.filter(_.children.isEmpty)

  //We'll define twins as two people who are children of the same person and have the same age.
  //(although this is not entirely accurate)
  def twins(person: Person): List[(Person, Person)] = {
    val list = person.children.sortBy(_.age)
    list.drop(1).zip(list).filter{case (a, b) => a.age == b.age}
  }

  def siblings4yDiff(person: Person): List[(Person, Person)] = {
    val list = person.children.sortBy(_.age)
    list.drop(1).zip(list).filter{case (a, b) => math.abs(a.age - b.age) > 4}
  }

  //Returns the people from the list passed as method parameter whose children are on average 4 years old.
  def childrenAvg4yo(people: List[Person]): List[Person] = people
    .filter(p => p
      .children
      .map(_.age)
      .sum/p.children.size == 4)

  def parentsOlderThan(people: List[Person], age: Int): List[Person] =
    if (people.isEmpty) Nil
    else people
      .filter(_.age > age)
      .flatMap(_.children) ++ parentsOlderThan(people.flatMap(_.children), age)
}

object PersonApp extends App {
  val p4 = Person("John", 2)
  val p3 = Person("Emily", 21)
  val p2 = Person("Mark", 26, List(p4))
  val p6 = Person("Morty", 3)
  val p7 = Person("Summer", 5)
  val p5 = Person("Jerry", 30, List(p6, p7))
  val p1 = Person("Tom", 53, List(p2, p3, p5))

  p1.descendants

  val i = Inquirer

  i.minorsAdults(p1)

  i.parentsOlderThan(List(p1), 40)

  i.twins(p1)
}


