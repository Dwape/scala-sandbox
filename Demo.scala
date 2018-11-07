import scala.io.Source

class Point

object DemoApp extends App {

  val lines = Source.fromFile("src/main/scala/test.txt").getLines().toList

  val words = lines
    .map(_.toLowerCase())
    .flatMap(_.split("[^a-z]").toList)
    .filter(_.nonEmpty)

  val freq = words
    .groupBy( e => e) //groups all instances of a word
    .map {
      case (word, list) => //word and a list with that word n times where n is the frequency of the word in the text.
        word -> list.length
    }

  val top = freq.toList.sortBy(-_._2).take(50) //- so that it is in descending order

  top.foreach(println)
}