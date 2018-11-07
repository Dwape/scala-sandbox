import scala.io.StdIn
import scala.util.Try

case class User(name: String, alias: Option[String] = None) // alias can have no value, it is optional

object FuncStructs extends App {

  def options(): Unit = {
    val users: List[User] = List (
      User("Pepe"),
      User("Juan", Some("juancito")),
      User("Jose", Some("Jose88")),
    )

    // map mantains the original structure
    val normalizedUsers: List[User] = users.map(
      u => u.copy(alias = u.alias.map(_.toLowerCase))
    )

    for (u <- normalizedUsers) {
      val id: String = u.alias.getOrElse("undefined")
      // if there is no value the default value, passed as method parameter, is used
      // we have to obtain the string from inside the option
      println(s"User ${u.name} (alias: [$id])")
    }
  }

  // trait Either[+L, +R]
  // case class Left[+L, +R](value: L) extends Either[L, R]
  // case class Right[+L, +R](value: R) extends Either[L, R]

  // right value is the correct one by convention
  // right value is the main value

  // either can have any two types
  def readInt: Either[Exception, Int] = {
    val in: String = StdIn.readLine("Enter an integer:")
    try {
      Right(in.toInt)
    }
    catch {
      case e: Exception =>
        Left(e)
    }
  }

  // when the left value of an Either is ana exception, we can use a Try
  // Left value is always a Throwable

  // trait Try[+T]
  // case class Success[+T](value: T) extends Try[T]
  // case class Failure[+T](e: Throwable) extends Try[T]
  def readInt2: Try[Int] = {
    val in: String = StdIn.readLine("Enter an integer:")
    Try(in.toInt) // try converts the Try to a Success or Failure

    // object Try {
    //  def apply[T](op: => T): Try[T] = {
    //    try {
    //      Success(op)
    //  }
    //  catch {
    //    case e: Exception =>
    //      Failure[T](e)
    //    }
    //  }
    // }
  }

  // val value = readInt

  //val value = readInt.map(_ + 1) // gives priority to the right element
  // map operates on the right value

  // println(value)

  val value: Try[Int] = readInt2.map(_ + 1)
  value.foreach(println)

  case class Person(id: String, firstName: Option[String], lastName: Option[String])

  val p = Person("a", Some("Pepe"), Some("Lopez"))

  val fullName = for {
    first <- p.firstName
    last <- p.lastName
  } yield first + " " + last

  fullName.foreach(println)
}

// trait Option[+T] {
// def map[U](f: T => U): Option[U]
// }
// case class Some[T](value: T) extends Option[T] {
// def map[U](f: T => U): Some(value)
// }
// case object None extends Option[Nothing] {
// def map[U](f: T => U): U = None
// }

// x: Option[String] = None
// y: Option[Int] = None

// null in Java works as if it extended from every other type, it is a subtype of all types