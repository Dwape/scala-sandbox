trait Cypher {
  def encode(message: String): String
  def decode(message: String): String
}

/*
case class ShiftCypher(k: Int) extends Cypher {

  def encode(message: String): String = code(message, k)

  def decode(message: String): String = code(message, -k)

  private def code(message: String, factor: Int): String =
    message.map({case ' ' => ' '
    case c => (Math.floorMod(c.toInt + factor - 'a'.toInt, 26) + 'a'.toInt).toChar})
}
*/

case class ShiftCypher2(k: Int) extends Cypher {

  def encode(message: String): String = code(message, k)

  def decode(message: String): String = code(message, 26-k)

  private def code(message: String, factor: Int): String =
    message.map({case ' ' => ' '
    case c => ((c.toInt + factor - 'a'.toInt) % 26 + 'a'.toInt).toChar})
}

case object ShiftCypher {

  def encode(message: String, k: Int): String =
    message.map({case ' ' => ' '
    case c => ((c.toInt + k - 'a'.toInt) % 26 + 'a'.toInt).toChar})

  def decode(message: String, k: Int): String = encode(message, 26-k)
}

object Cypher extends App {
  val cypher = ShiftCypher2(12)
  val cypherText = cypher.encode("this is an attempt at creating a very basic shift cypher")
  println(cypherText)
  println(cypher.decode(cypherText))

  val text = ShiftCypher.encode("hello this is some text", 13)
  println(text)
  println(ShiftCypher.encode(text, 13))
}
