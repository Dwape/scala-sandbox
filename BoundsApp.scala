trait Shape {
  def area: Int
}

case class Rectangle(h: Int, w: Int) extends Shape {
  override def area: Int = h * w
}

case class Triangle(h: Int, w:Int) extends Shape {
  override def area: Int = (h * w) / 2
}

case class GenericShapeContainer[+T <: Shape](shape: T) { // added a bound <: (upper bound)
  // T >: Shape (lower bound)
  // T extends Shape
  // We can make a triangle container (smaller than shape)
  def area: Int = shape.area
}

case class ShapeContainer(shape: Shape) {
  def area: Int = shape.area
}

object Objects {

  import Arithmetic.Adder

  case class P(x: Int, y: Int)

  implicit val ordering: Ordering[P] = Ordering.by(e => (e.x, e.y))

  // We can now add points without changing either Arithmetic or object "library"
  // Could be included in the library so that it is compatible with other libraries
  implicit val pAdder: Adder[P] = new Adder[P] {
    override def zero: P = P(0, 0)
    override def add(a: P, b: P): P = P(a.x + b.x, a.y + b.y)
  }

  implicit val numericP: Numeric[P] = new Numeric[P] {
    override def plus(x: P, y: P): P = P(x.x + y.x, x.y + y.y)

    override def minus(x: P, y: P): P = P(x.x - y.x, x.y - y.y)

    override def times(x: P, y: P): P = P(x.x * y.x, x.y * y.y)

    override def negate(x: P): P = P(-x.x, -x.y)

    override def fromInt(x: Int): P = P(x, x)

    override def toInt(x: P): Int = x.x

    override def toLong(x: P): Long = x.x

    override def toFloat(x: P): Float = x.x

    override def toDouble(x: P): Double = x.x

    override def compare(x: P, y: P): Int = 0
  }
}

// polymorphism in object oriented languages doesn't allow to add behavior to classes we don't own
// (such as libraries)
object Arithmetic {

  trait Adder[T] {
    def zero: T
    def add(a: T, b: T): T
  }

  def sum1[T](list: List[T]) (implicit adder: Adder[T]): T = {
    list.foldLeft(adder.zero)(adder.add)
  }

  // In the context of an Adder
  // It works wherever there is an Adder of T
  def sum2[T : Adder](list: List[T]): T = { // ad hoc polymorphism
    val adder = implicitly[Adder[T]]
    list.foldLeft(adder.zero)(adder.add)
  }

  implicit val intAdder: Adder[Int] = new Adder[Int] {
    override def zero: Int = 0
    override def add(a: Int, b: Int): Int = a + b
  }

  implicit val doubleAdder: Adder[Double] = new Adder[Double] {
    override def zero: Double = 0
    override def add(a: Double, b: Double): Double = a + b
  }
}

object BoundsApp extends App {

  import Objects._
  import Arithmetic._

  val value: GenericShapeContainer[Rectangle] = GenericShapeContainer(Rectangle(10, 10))

  // -- View Bounds --
  //def area[A <% Shape](shape: A): Int = shape.area // deprecated
  // any type that can be seen as a shape (which can be converted to shape)

  def area[A](shape: A)(implicit a: A => Shape): Int = shape.area // can retain type
  // receives an implicit function which can convert from A to Shape
  // the function is used in case the parameter is not a shape

  def area2(shape: Shape): Int = shape.area

  implicit def pointToShape(p: P): Shape = new Shape {
    override def area: Int = p.x + p.y
  }

  //println(area(P(10, 10)))

  val l1 = List(1, 2, 3)
  val l2 = List(1.0, 2.0, 3.0)
  val l3 = List(P(0, 1), P(2, 3))

  // Too specific (only works for Int)
  // Receives initial value in case there is an empty list.
  /*
  def sum1(list: List[Int]): Int = {
    list.foldLeft(0)(_ + _)
  }
  */
  // we have to pass an adder as method parameter every time we use the function
  /*
  def sum1[T](list: List[T], adder: Adder[T]): T = {
    list.foldLeft(adder.zero)(adder.add)
  }
  */

  // can use the function without using an adder as long as it is defined in the environment

  val r1: Int = sum1(l1)
  val r2: Double = sum1(l2)

  val r3: P = sum1(l3)

  println(l3.sum)
}


