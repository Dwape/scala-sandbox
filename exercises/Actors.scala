package exercises

import akka.actor.{Actor, ActorSystem, Props}

case class Measurement(name: String, value: Double)

case class GetStats(name: String)

case class Reset(name: String)

case class Stats(name: String, min: Double, max: Double, total: Double, count: Int)

case class Error(message: String = "There has been an error")

class Statistics extends Actor {

  var values: Map[String, List[Double]] = Map()

  /*
  override def receive: Receive = {
    case Measurement(name, value) =>
      //val list = values(name)
      val list = values.get(name)
      if (list.isDefined) values = values + (name -> (value :: list.get))
      else values = values + (name -> List(value))
    case GetStats(name) =>
      val list = values.get(name)
      if (list.isDefined) sender() ! Stats(name, list.get.min, list.get.max, list.get.sum, list.get.size)
      else sender () ! Error("There are no measurements with that name")
    case Reset(name) =>
      values = values - name
  }
  */

  override def receive: Receive = {
    case Measurement(name, value) =>
      values.get(name) match {
        case Some(l) => values = values + (name -> (value :: l))
        case None => values = values + (name -> List(value))
      }
    case GetStats(name) =>
      values.get(name) match {
        case Some(l) => sender() ! Stats(name, l.min, l.max, l.sum, l.size)
        case None => sender () ! Error("There are no measurements with that name")
      }
    case Reset(name) =>
      values = values - name
  }
}

class StatisticsPrinter extends Actor {
  override def receive: Receive = {
    case Stats(name, min, max, total, count) =>
      println(s"[Stats] name:$name min:$min max:$max total:$total count:$count")
    case Error(msg) =>
      println(s"[Error] $msg")
  }
}

object ActorApp extends App {
  val system = ActorSystem("crawler")
  val statistics = system.actorOf(Props[Statistics], "statistics")
  val printer = system.actorOf(Props[StatisticsPrinter], "printer")

  //fetcher.tell(FetchURL("https://scala-lang.org"), printer)
  //fetcher.tell(FetchURL("http://scala-android.org"), printer)

  //val html = "<html><title>Hello</title><a href ='about.html'></a></html>"
  statistics.tell(Measurement("hello", 10), printer)
  statistics.tell(Measurement("hello", 20), printer)
  statistics.tell(GetStats("hello"), printer)
  statistics.tell(GetStats("hell"), printer)
  statistics.tell(Reset("hello"), printer)
  statistics.tell(GetStats("hello"), printer)
  statistics.tell(Reset("hell"), printer)
}
