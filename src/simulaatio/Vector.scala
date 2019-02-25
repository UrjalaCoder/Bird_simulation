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

