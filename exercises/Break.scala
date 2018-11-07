package exercises

object Break {

  private class BreakException extends RuntimeException
  private val breakException = new BreakException

  def breakable(op: => Unit): Unit = {
    try {
      op
    } catch {
      case ex: BreakException =>
    }
  }

  def break: Unit = {
    throw breakException
  }
}

object BreakApp extends App {
  import Break._

  breakable {
    for (i <- 1 to 100) {
      println(i)
      if (i == 10) {
        break
      }
    }
  }
}

