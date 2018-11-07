package exercises

import scala.io.Source

object Freq {

  def calcWordFreq(file: String): List[(String, Int)] = {
    val lines = Source.fromFile(file).getLines().toList

    val words = lines
      .map(_.toLowerCase())
      .flatMap(_.split("[^a-z]").toList)
      .filter(_.nonEmpty)

    val freq = words
      .groupBy(e => e)
      .map {
        case (word, list) =>
          word -> list.length
      }

    freq.toList
  }
}

object TestApp extends App {
  println(Freq.calcWordFreq("src/main/scala/exercises/test"))
}
