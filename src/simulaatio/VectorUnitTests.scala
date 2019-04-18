package simulaatio

object Vector2UnitTests extends App{
  val a = new Vector2(1.0, 2.0)
  val b = new Vector2(5.0, -2.0)
  
  // Dot product unit test
  println(if(a.dot(b) == 1) "[DOT PRODUCT] Success!" else "[DOT PRODUCT] Failed!")
  
  // Vector2 magnitude
  println(if((a.mag) == Math.sqrt(5.0)) "[MAGNITUDE] Success!" else "[MAGNITUDE] Failed!")
  
  // Vector2 addition and subtraction
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
  
  // Vector2-scalar multiplication
  val scaledA = a * 2
  if(scaledA.x == 2.0 && scaledA.y == 4.0) {
    println("[SCALING] Success!")
  } else {
    println("[SCALING] Failed!")
  }
  
  // Unit Vector2
  val unitB = b.unit
  val bMag = b.mag
  if(unitB.x == (b.x / b.mag) && unitB.y == b.y / b.mag) {
    println("[UNIT Vector2] Success!")
  } else {
    println("[UNIT Vector2] Failed!")
  }
}