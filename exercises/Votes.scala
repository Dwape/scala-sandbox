package exercises

import scala.io.Source

case class Result(province: String, district: String, candidate: Char, votes: Int)

object Votes {

  def getResults(file: String): List[Result] = {
    val lines = Source.fromFile(file).getLines().toList
    lines.map(_.split(",").map(_.trim)).map(a => Result(a(0), a(1), a(2)(0), a(3).toInt)) // isn't a an array?
  }

  def totalVotes(results: List[Result]): Int = {
    results.map(_.votes).sum
  }

  def winner(result: List[Result]): Char = {
    result.groupBy(e => e.candidate).map {
      case (c, r) =>
        c -> r.map(_.votes).sum
    }.toList.maxBy(_._2)._1
  }

  def bestProvince(result: List[Result], candidate: Char): String = {
    result
      .filter(_.candidate == candidate)
      .groupBy(_.province).map {
      case (c, results) =>
        c -> results.map(_.votes).sum
    }
      .toList
      .maxBy(_._2)._1
  }
}

object VotesApp extends App {
  val results = Votes.getResults("src/main/scala/exercises/test")
  println(Votes.totalVotes(results))
  println(Votes.winner(results))
  println(Votes.bestProvince(results, 'A'))
}
