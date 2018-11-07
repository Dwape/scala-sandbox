object DoWhile extends App {

  import Retry._

  var i = 0
  retryable {
    i = i + 1
    println(i)
    retry (i < 10)
  }
}

object Retry {

  def retryable(op: => Boolean): Unit = {
    if (op) {
      retryable(op)
    }
  }

  def retry(condition: Boolean): Boolean = condition
}