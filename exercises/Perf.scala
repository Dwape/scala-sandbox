package exercises

object Perf {

  def perf(name: String)(op: => Unit): Unit = {
    val start = System.currentTimeMillis()
    op
    val end = System.currentTimeMillis()
    println(s"Block $name executed in ${end-start} milliseconds")
  }
}

object PerfApp extends App {
  import Perf._

  val list = List("a", "b", "c", "d", "e", "f", "g")

  perf("Hello") {
    list.size
  }
}
