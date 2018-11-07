object Random {

  var seed = 123

  def next: Int = {
    seed = seed * seed
    seed
  }
}

object RandomApp extends App {
  println(Random.next)
  println(Random.next)
  println(Random.next)
  println(Random.next)
  println(Random.next)
  println(Random.next)
}
