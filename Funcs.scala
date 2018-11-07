object Funcs extends App {

  def mul(n1: Int, n2: Int): Int = n1 * n2 //we can declare functions anywhere, even inside other functions

  def mul2(n1: Int)(n2: Int): Int = n1 * n2

  mul(2, 2)
  mul2(2)(2)

  // double is a function which takes an integer and returns and integer
  val double: Int => Int = mul2(2)(_) // failed attempt to implement curry functions
  //val double2: Function1[Int, Int] = double // this is the same

  //functions are also objects

  //print(double(3))

  def f1(p: Int => Int): Unit = { // high order functions
    println("p: " + p(5))
  }

  val myFunction: Int => Int = (n: Int) => n + 1

  f1(myFunction)

  //f1((n: Int) => n + 1) // these three lines do the same thing
  //f1(n => n + 1)
  //f1(_ + 1)

  // partial function
  // this function can interpret two types of data, Int and String
  val partialFunction: PartialFunction[Any, Int] = {
    case n: Int =>
      n + 2
    case s: String =>
      s.length
  }

  def completeFunction(v: Any): Int =  {
    v match {
      case n: Int =>
        n + 2
      case s: String =>
        s.length
    }
  }

  //println(partialFunction(true))
  println(completeFunction(4))

  println(Math.floorMod(-5, 26))

  if (partialFunction.isDefinedAt(true)) {
    partialFunction(true)
  }

  List("hello", 12, true).collect { // works like map but only for the values for which the partial function is defined
    case n: Int =>
      n
  }

  def f1(p: Int): Unit = {
    println("f1.start")

    p
    p

    println("f1.end")
  }

  f1({
    println("Generating value for f1") // this is evaluated before f1
    10
  })

  def f2(p: () => Int): Unit = {
    println("f2.start")

    p()
    p()

    println("f2.end")
  }

  f2(() => {
    println("Generating value for f2") // the function is executed when it is called.
    10
  })

  // p: Int // does the evaluation lazily
  def f3(p: => Int): Unit = {
  println("f3.start")

  p
  p

  println("f3.end")
  }

  f3({println("Gen 10"); 10})

  def f4(cond: Boolean, p: => Int): Unit = {
    println("f4.start")

    if (cond) p

    println("f4.end")
  }

  f4(false, {println("Gen 10"); 10})

  println("Testing log...")

  val list = List(1, 2, 3)

  MyLog.enabled = true

  MyLog.logDebug("List values:" + list)

  object MyLog {
    var enabled = true

    // the function won't be evaluated if enable is disabled
    def logDebug(msg: => String): Unit = {
      if (enabled) {
        println("DEBUG: " + msg)
      }
    }
  }
}