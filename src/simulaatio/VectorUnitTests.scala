package simulaatio

object VectorUnitTests extends App{
  val a = new Vector(1.0, 2.0)
  val b = new Vector(5.0, -2.0)
  
  // Dot product unit test
  println(if(a.dot(b) == 1) "[DOT PRODUCT] Success!" else "[DOT PRODUCT] Failed!")
  
  // Vector magnitude
  println(if((a.mag) == Math.sqrt(5.0)) "[MAGNITUDE] Success!" else "[MAGNITUDE] Failed!")
  
  // Vector addition and subtraction
  val c = a + b
  if(c.x == 6.0 && c.y == 0) {    
	  println("[ADDITION] Success!")
  } else {
    println("[ADDITION] Failed!")
  }
  
  val d = a - b
  if(d.x == -4.0 && d.y == 4.0) {
    println("[SUBTRACTION] Success!")
  } else {
    println("[SUBTRACTION] Failed!")
  }
  
  // Vector-scalar multiplication
  val scaledA = a * 2
  if(scaledA.x == 2.0 && scaledA.y == 4.0) {
    println("[SCALING] Success!")
  } else {
    println("[SCALING] Failed!")
  }
  
  // Unit vector
  val unitB = b.unit
  val bMag = b.mag
  if(unitB.x == (b.x / b.mag) && unitB.y == b.y / b.mag) {
    println("[UNIT VECTOR] Success!")
  } else {
    println("[UNIT VECTOR] Failed!")
  }
}