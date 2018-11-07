import scala.concurrent.Future
import scala.io.{Source, StdIn}
import scala.util.{Failure, Success}

object FuturesApp extends App {

  import scala.concurrent.ExecutionContext.Implicits.global // an implicit thread pool

  def downloadSync(url: String): String = {
    Source.fromURL(url).getLines().mkString("\n")
  }

  def downloadAsync(url: String): Future[String] = {
    Future {
      Source.fromURL(url).getLines().mkString("\n")
    }
  }

  def countChars(url: String): Future[Int] = {
    downloadAsync(url).map(_.length)
  }

  // future needs a thread pool (receives it implicitly)
  /*
  Future {
    val c1  = downloadSync("https://scala-lang.org")
    println("c1: " + c1.take(20))
  }

  Future {
    val c2  = downloadSync("http://scala-android.org")
    println("c2: " + c2.take(20))
  }

  StdIn.readLine("Press ENTER to complete\n")
  */

  /*
  val c1: Future[String] = downloadAsync("https://scala-lang.org")
  val c2: Future[String] = downloadAsync("http://scala-android.org")

  val size: Future[Int] = c1.map(_.length)

  size.foreach { s =>
    println(s"Size: $s")
  }
  */

  /*
  c1.onComplete{
    case Success(v) =>
      println("Success: " + v.take(10))
    case Failure(e) =>
      println("Failure: " + e)
  }

  c2.foreach{ v =>
    println("value: " + v)
  }
  */
  val s1: Future[Int] = countChars("https://scala-lang.org")
  val s2: Future[Int] = countChars("http://scala-android.org")
  val s3: Future[Int] = countChars("http://scala-android.org")
  // how do we combine the two results?
  // they are both containers

  /*
  val result: Future[Int] = s1.flatMap { v1 =>
    s2.flatMap { v2 =>
      s3.map { v3 =>
        v1 + v2 + v3
      }
    }
  }
  */

  // for combines structures that have a map and flatMap
  val result2: Future[Int] = for {
    v1 <- s1
    v2 <- s2
    v3 <- s3
  } yield v1 + v2 + v3

  result2.foreach { total =>
    println("total: " + total)
  }

  println("Press ENTER to complete\n")
  StdIn.readLine("")
}