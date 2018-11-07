package crawler

import java.net.{MalformedURLException, URL}

import akka.actor._
import akka.routing.RoundRobinPool

import scala.io.Source
import scala.util.Try

/**
  * Protocol
  */
case class FetchURL(url: String)

case class FetchedURL(usl: String, content: String)

case class Parse(url: String, content: String)

case class Parsed(url: String, page: WebPage)

case class WebPage(title: String, links: List[String])

case class Crawl(url: String, maxPages: Int)

case class FoundPage(rootUrl: String, url: String, page: WebPage)

/**
  * Fetcher
  */
class Fetcher extends Actor {
  //val workers: ActorRef = context.actorOf(Props[FetchWorker]) // Create children of an actor
  // Creates many actors (4) and distributes the messages
  val workers: ActorRef = context.actorOf(RoundRobinPool(4).props(Props[FetchWorker]))
  override def receive: Receive = {
    case msg: FetchURL =>
      workers.forward(msg) // Forward the message (the sender is the original one)
  }
}

class FetchWorker extends Actor {
  override def receive: Receive = {
    case FetchURL(url) =>
      try {
        println(s"[FetchWorker] Downloading $url")
        val content = Source.fromURL(url).getLines().mkString("\n")
        sender() ! FetchedURL(url, content)
      }
      catch {
        case e: Exception =>

      }
  }
}

/**
  * Parser
  */
class Parser extends Actor {
  //val workers: ActorRef = context.actorOf(Props[FetchWorker]) // Create children of an actor
  // Creates many actors (4) and distributes the messages
  val cores = Runtime.getRuntime.availableProcessors()
  val workers: ActorRef = context.actorOf(RoundRobinPool(cores).props(Props[ParseWorker]))
  override def receive: Receive = {
    case msg: Parse =>
      workers.forward(msg) // Forward the message (the sender is the original one)
  }
}

class ParseWorker extends Actor {
  override def receive: Receive = {
    case Parse(url, html) =>
      try {
        import scala.collection.JavaConverters._

        val current = new URL(url)
        val doc = org.jsoup.Jsoup.parse(html)
        val links = doc
          .select("a[href]")
          .asScala
          .map(_.attr("href")) // It was a java list

        val urls = links.flatMap(
          l => Try(new URL(current, l)).toOption
        ).map(_.toString.takeWhile(_ != '#')).distinct.sorted // returns a list of valid URLs

        val page = WebPage(doc.title(), urls.toList)

        sender() ! Parsed(url, page)
      }
      catch {
        case _: MalformedURLException =>
      }
  }
}

/**
  * Crawler
  */
class Crawler extends Actor {
  val fetcher = context.actorOf(Props[Fetcher])
  val parser = context.actorOf(Props[Parser])

  var root: String = _
  var originalSender: ActorRef = _

  var pending: Set[String] = Set()
  var processed: Set[String] = Set()

  override def receive: Receive = {
    case Crawl(url, maxPages) =>
      root = url
      originalSender = sender()

      fetcher ! FetchURL(url)

    case FetchedURL(url, content) =>
      parser ! Parse(url, content)

    case Parsed(url, webPage) =>
      originalSender ! FoundPage(root, url, webPage)

      pending = pending ++ (webPage.links.toSet -- processed)

      pending.takeRight(10).foreach { p =>
        processed += p
        fetcher ! FetchURL (p)
      }

      pending = pending.dropRight(10)
  }
}

class CrawlerPrinter extends Actor {
  override def receive: Receive = {
    case FetchedURL(url, content) =>
      println(s"[Printer] FetchedURL: $url, ${content.take(20)}")
    case Parsed(url, page) =>
      println(s"[Printer] Parsed: $url with ${page.links.size} links")
    case FoundPage(root, url, page) =>
      println(s"[Printer] Found Page $url")
  }
}

// Actors are independent
object FetcherApp extends App {
  val system = ActorSystem("crawler")
  //val fetcher = system.actorOf(Props[Fetcher], "fetcher")
  //val parser = system.actorOf(Props[Parser], "parser")
  val printer = system.actorOf(Props[CrawlerPrinter], "printer")

  val crawler = system.actorOf(Props[Crawler], "crawler")

  //fetcher.tell(FetchURL("https://scala-lang.org"), printer)
  //fetcher.tell(FetchURL("http://scala-android.org"), printer)

  //val html = "<html><title>Hello</title><a href ='about.html'></a></html>"
  crawler.tell(Crawl("https://scala-lang.org", 10000), printer)
}
