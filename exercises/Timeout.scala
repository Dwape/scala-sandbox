package exercises

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Timeout {

  def timeout(millis: Long)(op: => Unit): Unit = {
    /*
    Future {
      val future = Future {
        op
      }
      Thread.sleep(millis)
      if (!future.isCompleted) new Exception
    }
    */
    Future {
      val future = Future {
        op
      }
      Thread.sleep(millis)
      if (!future.isCompleted){
        new Exception
      }
    }
  }
}

object TimeoutApp extends App{
  import Timeout._

  timeout(10) {
    for (i <- 1 to 10) {
      println(i)
    }
  }
  Thread.sleep(10000000)
}
