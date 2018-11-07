package main.scala.demo

class PointOld(_x: Int, _y: Int) {
  require(_x >= 0)
  require(_y >= 0)

  val x: Int = _x
  val y: Int = _y

  println("Created point " + x + ", " + y)

  def add(that: PointOld): PointOld = new PointOld(x + that.x, y + that.y)

  override def toString: String = "Point(" + x + ", " + y + ")"

}// equals or apply are not implemented, they need to be implemented manually.

case class Point(x: Int, y: Int) {
  def sum : Int = x + y

  // different ways of creating the negative point
  // def negate: Point = Point(-x, -y)

  // the first time it is asked for, it is created
  // subsequent calls just return the created values
  lazy val negate: Point = Point(-x, -y)

  def add(other: Point): Point = {
    Point(x + other.x, y + other.y)
  }
  def +(other: Point): Point = {
    Point(x + other.x, y + other.y)
  }
}// automatically implements several methods, el eq, el apply y unapply
// the apply method by default creates an instance of the object.
// works with pattern matching
// apply is like a constructor (or a kind of factory).
// when we have a function func, we can apply it by using parentheses as follows: func()
// it is the same as func.apply(), but if the apply call is avoided, the compiler looks for the apply function

object PointApp extends App{
  val a1 = new PointOld(2,3)
  val a2 = PointOld(3,3) //calls the apply method of PointOld

  val a4 = PointOld(0,0) // doesn't create a new instance (uses the existing one).
  // this can be done safely because objects are immutable (so they can be shared in several places)

  val a3 = PointOld.minX(a1, a2)

  val a = Point(0,5) // no need to use new key word because the case class has apply
  val b = Point(3,3)

  val sum1 = a.add(b)
  val sum2 = a + b

  val Point(x, y) = a

  PointOld.ZERO // reference the attribute of the object

  val str = a match {
    case Point(0, 0) => "origin"
    case Point(_, 0) => "axis"
    case Point(0, _) => "axis"
    case Point(x, y) => s"x: $x, y: $y"// the s allows to use $ to insert strings (string formatting).
  }

  println(str)
  print(a==b)

  //works as a static function, independent of an instance
}

object PointOld {//this can be used to define static methods

  val ZERO = new PointOld(0, 0)

  def apply(_x: Int, _y: Int): PointOld = if (_x == 0 && _y == 0) ZERO else new PointOld(_x, _y)
  def minX(p1: PointOld, p2: PointOld): PointOld = {
    if (p1.x < p2.x) p1 else p2
  } //the object has the same name as the class
}

case object Ack