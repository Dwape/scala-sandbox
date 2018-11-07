
// no difference between primitive types and non primitive types behavior-wise.
// there is a difference in performance, due to JVM.
object ValueClasses extends App{

  class Meter(val value: Double) extends AnyVal { // we can make out own primitive values.
    // this will be a primitive value
    // only one constructor with one argument
    // can only have one attribute
    def +(m: Meter): Meter = new Meter(value + m.value)
  }

  // only performance is affected doing this.
  // functionally they are the same.
  val m1 = new Meter(1.2)
  val m2 = new Meter(1.3)

  val m3 = m1 + m2

  val v1 = 1.2
  val v2 = 1.3

  val v3 = v1 + v2
}


