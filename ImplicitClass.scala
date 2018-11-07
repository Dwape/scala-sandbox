
object Strings { // class is inside of the object
  // nested class inside of the object
  implicit class Str(str: String) { // the implicit class receives only one parameter
  // we can converts a string into a Str automatically.
    def wow: String = str.toUpperCase + "... WOW !!!"
  }
}

object Booleans {
  implicit class MyBool(x: Boolean) {
    def and(that: MyBool): MyBool = if (x) that else this
    def or(that: MyBool): MyBool = if (x) this else that
    def negate: MyBool = new MyBool(!x)
  }
}

object ImplicitClass extends App {
  import Strings._
  import Booleans._

  val s = "hello"

  print(s.wow)

  val b1 = true
  val b2 = false
  val b3 = true

  print(b1 and b2)
  //new Str(s).wow
}
