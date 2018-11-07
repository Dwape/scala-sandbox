import scala.io.Source

object Collections extends App {

  val urls = List (
    "http://scala-lang.org",
    "http://scala-android.org",
    "http://scala-lang.org",
    "http://scala-android.org",
    "http://scala-lang.org",
    "http://scala-android.org",
    "http://scala-lang.org",
    "http://google.com",
    "http://scala-android.org",
  )

  // Normally, function evaluation is strict in Scala
  // Evaluates the functions in map for every element even if it will not be needed

  // Imperative -> tell the algorithm what to do step by step
  // Declarative -> tell the algorithm what we want to do, not how

  val content = urls
    //.view // only evaluates the functions if it is necessary
    .par // parallel
    .filter(_.endsWith(".org"))
    .map { u =>
      println(s"Downloading $u")
      Source.fromURL(u).getLines().mkString("\n")
    }

  content.take(4).foreach { c =>
    println(c.length)
  }

  // Spark -> framework in Scala
  // collections in a distributed system
}
