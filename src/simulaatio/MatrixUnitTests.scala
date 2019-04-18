package simulaatio

object MatrixUnitTests extends App {
  val matrix = new Matrix2(new Vector2(1, -4), new Vector2(1, 3))
  
  // Vector2-matrix multiplication
  val v = new Vector2(1, -2)
  var result = matrix * v
  if(result.x == -1 && result.y == -10) {
    println("[MATRIX Vector2 MULTIPLICATION] Success!")
  } else {
    println("[MATRIX Vector2 MULTIPLICATION] Failed!")
  }
  
  // Constant multiplication
  val c = 3.0
  val resultConstant = matrix * c
  var firstCol = new Vector2(3, -12)
  var secondCol = new Vector2(3, 9)
  val firstSuccess = resultConstant.elements(0).x == firstCol.x && resultConstant.elements(0).y == firstCol.y
  val secondSuccess = resultConstant.elements(1).x == secondCol.x && resultConstant.elements(1).y == secondCol.y
  if(firstSuccess && secondSuccess) {
    println("[MATRIX CONSTANT MULTIPLICATION] Success!")
  } else {
    println("[MATRIX CONSTANT MULTIPLICATION] Failed!")
  }
  
  // Determinant
  val det = matrix.determinant
  if(det == 7) {
    println("[DETERMINANT] Success!")
  } else {
    println("[DETERMINANT] Failed!")
  }
  
  // Inverse matrix
  firstCol = new Vector2(3 / 7.0, 4 / 7.0)
  secondCol = new Vector2(-1 / 7.0, 1 / 7.0)
  val inverse = matrix.inverse
  val successA = inverse.elements(0).x == firstCol.x && inverse.elements(0).y == firstCol.y
  val successB = inverse.elements(1).x == secondCol.x && inverse.elements(1).y == secondCol.y
  if(successA && successB) {
    println("[INVERSE MATRIX] Success!")
  } else{
    println("[INVERSE MATRIX] Failed!")
  }
  
  
}