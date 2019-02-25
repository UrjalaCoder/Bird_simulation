package simulaatio

import java.awt.Graphics2D
import scala.collection.mutable.Buffer

class Bird(private var currentPosition: Vector, initialVelocity: Vector) {
  private var currentVelocity = initialVelocity
  private var force = new Vector(0, 0)
  private var localBirds = Buffer[Bird]()
  
  // Basis vectors of local space iHat and jHat
  // jHat points in the direction of velocity
  def jHat = {
   this.velocity.unit 
  }
  
  // iHat always points perpendicular to the jHat.
  // iHat is always to the right of the bird's location.
  def iHat = {
    // Cross product of j and k vector.
    new Vector(this.jHat.y, -this.jHat.x)
  }
  
  def position = this.currentPosition
  def velocity = this.currentVelocity
  
  // Constants for drawing.
  private val wingAngle: Double = Math.PI / 8
  private val wingLength: Double = 30.0
  
  def drawBirds(g: Graphics2D) = {
    // Vectors that represent the left and right wings of the bird.
    val a = this.iHat * -math.sin(this.wingAngle) * this.wingLength - this.jHat * math.cos(this.wingAngle) * this.wingLength
    val b = this.iHat * math.sin(this.wingAngle) * this.wingLength - this.jHat * math.cos(this.wingAngle) * this.wingLength
    
    // Global position vectors of the tips of the wings.
    val left = this.position + a
    val right = this.position + b
    
    // TODO: Draw the shape
  }
}