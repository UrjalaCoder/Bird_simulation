package simulaatio

import math.sqrt
import math.pow

/*
 * Vector helper class. Implements some common operations.
 * 2-dimensional.
 */
class Vector(val x: Double, val y: Double) {
  
  // Magnitude of vector
  def mag: Double = {
    math.sqrt(math.pow(this.x, 2) + math.pow(this.y, 2))
  }
  
  // Is the vector parallel. Either same direction or opposite direction.
  def isParallel(other: Vector): Boolean = {
    math.abs(this.dot(other)) == 1
  }
  
  // Is the vector perpendicular.
  def isPerpendicular(other: Vector): Boolean = {
    this.dot(other) == 0
  }
  
  // Dot product with another vector.
  def dot(other: Vector) = {
    this.x * other.x + this.y * other.y
  }
  
  // Multiplication and division by a SCALAR.
  def *(scalar: Double): Vector = {
    new Vector(this.x * scalar, this.y * scalar)
  }
  
  def /(scalar: Double): Vector = {
    this.*(1.0 / scalar)
  }
  
  // Unit vector
  def unit: Vector = {
    new Vector(this.x / this.mag, this.y / this.mag)
  }
  
  // Add and subtract
  def +(other: Vector): Vector = {
    new Vector(this.x + other.x, this.y + other.y)
  }
  
  def -(other: Vector): Vector = {
    new Vector(this.x - other.x, this.y - other.y)
  }
  
  override def toString(): String = {
    s"(${this.x}, ${this.y})"
  }
}

// Simple matrix class
class Matrix2(firstCol: Vector, secondCol: Vector) {
  val elements = Array(firstCol, secondCol)
  
  // Vector-matrix multiplication.
  def *(v: Vector) = {
    val x = this.elements(0).x*v.x + this.elements(1).x * v.y
    val y = this.elements(0).y*v.x + this.elements(1).y * v.y
    new Vector(x, y)
  }
  
  def determinant: Double = {
    this.elements(0).x * this.elements(1).y - this.elements(1).x * this.elements(0).y
  }
  
  // Constant multiplication.
  def *(s: Double) = {
    new Matrix2(this.elements(0) * s, this.elements(1) * s)
  }
  
  // Assumed that matrix is invertible e.g determinant is 0
  // If det(M) == 0 then return the original matrix.
  // Should't matter too much because most of the time det(M) != 0. So the times inverse returns the original
  // are very uncommon.
  def inverse: Matrix2 = {
    if(this.determinant == 0) {
      this
    } else {      
    	new Matrix2(new Vector(this.elements(1).y, -this.elements(0).y), new Vector(-this.elements(1).x, this.elements(0).x)) * (1.0 / this.determinant)
    }
  }
}

