import ImplicitsApp.Conf

object Logs {

  def printError(msg: String)(implicit conf: Conf): Unit = { //configuration can be implicit
    val indentStr = " " * conf.indent
    println(s"$indentStr[ERROR] [App: ${conf.app}] $msg")
  }

  def printInfo(msg: String)(implicit conf: Conf): Unit = {
    val indentStr = " " * conf.indent
    println(s"$indentStr[INFO] [App: ${conf.app}] $msg")
  }

  implicit val defaultValues: Conf = Conf(4, "ImplicitApp")

}

object ImplicitsApp extends App {

  case class Conf(indent: Int, app: String)
  // implicit parameters.

  import Logs._

  printInfo("Loading App")
  printInfo("Loading config...")
  printError("Oops...")
}
