object Jsons extends App{

  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import io.circe.syntax._

  val book = Book(
    title = "Programming in Scala",
    author = "Martin Odersky",
    categories = List("programming", "scala")
  )

  val json: String = book.asJson.spaces2
  println(json)

  val newBook = decode[Book](json)
  println(newBook)
}

case class Book (
  title: String,
  author: String,
  categories: List[String]
  )

