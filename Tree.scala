
sealed trait IntTree { // As it is sealed,we can't create a subclass of the trait in another file
  def sum: Int // the method needs to be implemented in the classes that extend the interface
  // we can add a default implementation
}//similar to a Java interface but interfaces can implement methods
//The difference between traits and abstract classes is that a class can only inherit from one abstract class but many
// traits
//abstract class Tree

case class IntBranch(left: IntTree, right: IntTree) extends IntTree {
  override def sum: Int = left.sum + right.sum
}
case class IntLeaf(value: Int) extends IntTree {
  override def sum: Int = value
}

/**
  * Generic Tree
 */
sealed trait Tree[+T] // we know that the type is immutable
// creates types

// branch and leaf are subtypes.
case class Branch[T](left: Tree[T], right: Tree[T]) extends Tree[T] // creates data
case class Leaf[T](value: T) extends Tree[T] // creates data

object TreeApp extends App {

  type StringTree = Tree[String]
  type AnyTree = Tree[Any]
  type FreqMap = Map[String, Int]
  type GFreqMap[T] = Map[T, Int]

  def printTree(t: Tree[Any]): Unit = t match {
    case Branch(l, r) =>
      println("Left:")
      printTree(l)
      println("Right:")
      printTree(r)
    case Leaf(v) =>
      println(s"Value: $v")

  }

  val tree: Tree[Int] = Branch(
    Branch(
      Leaf(1), Leaf(2)
    ),
    Leaf(4)
    )

  printTree(tree)

  val treeS: Tree[String] = Leaf("Test")
}

object IntTreeApp extends App {

  def sumLeaves(t: IntTree): Int = t match {
    case IntBranch(l, r) => sumLeaves(l) + sumLeaves(r)
    case IntLeaf(v) => v
  }

  val tree = IntBranch(IntLeaf(1), IntLeaf(2))
  val tree2 = IntBranch(IntLeaf(1), IntLeaf(3))

  // println(tree == tree2) // recursive comparison
  // functional languages cannot have graphs with cycles
  // because objects are not mutable

  println(tree.sum) // object oriented
  // encapsulation
  // changing the structure won't break methods that used it
  // hard to work on a structure from the outside
  println(sumLeaves(tree)) // functional programming
  // easier to extend behavior by someone who is not the owner
  // functionality is external to the structure
}
