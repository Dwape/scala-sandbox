class A {
  def value: String = "A"
}

trait T1 {
  def helloT1 = "I'm T1"
  def hello = "I'm T1"
}

trait T2 {
  def helloT2 = "I'm T2"
  def hello = "I'm T2"
}

class B extends T1 with T2 { //
  override def hello: String = super[T2].hello // we specify it should use hello from T2
}

class Person2(val name: String)

trait MutableChildren {
  private var children: List[Person2] = Nil

  def addChild(child: Person2): Unit = children = child :: children
  def getChildren: List[Person2] = children
}

trait Friendly {
  def hello(name: String) = s"Hello $name"
}

object Mixins extends App { // mixes the functionality of both interfaces.
  print(new B().hello)

  // Person with MutableChildern
  type FriendlyParent = Person with MutableChildren with Friendly
  val person = new Person2(name = "Juan") with MutableChildren with Friendly
  // this instance if of type Person with MutableChilden
  person.addChild(new Person2("X"))
  println(person.hello("Z"))
}
