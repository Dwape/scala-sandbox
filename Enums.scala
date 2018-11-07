import Enums.WeekDay

object Enums {

  object WeekDay extends Enumeration {
    type WeekDay = Value
    val Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday = Value // value is inherited from Enumeration
  }
}

object EnumApp extends App {

  import WeekDay._

  val v: WeekDay = WeekDay.Friday

  //@Deprecated //annotations are like in Java
  print(v)
}