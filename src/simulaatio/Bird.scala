package simulaatio

import java.awt.Graphics2D
import scala.collection.mutable.Buffer

class Bird(private var currentPosition: Vector, initialVelocity: Vector, private val behaviour: Behaviour) {
  private var currentVelocity = initialVelocity
  private var localBirds = Array[Bird]()
  private val mass = 10.0
  
  // Basis vectors of local space iHat and jHat
  // jHat points in the direction of velocity
  def jHat = {
   if(this.velocity.x == 0 && this.velocity.y == 0) {
     new Vector(0, 1)
   } else {     
	   this.velocity.unit 
   }
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
  
  def distanceToBird(other: Bird) = {
    val deltaX = math.pow(other.position.x - this.position.x, 2)
    val deltaY = math.pow(other.position.y - this.position.y, 2)
    math.sqrt(deltaX + deltaY)
  }
  
  // Local space basis vector matrix
  def basisVectorMatrix: Matrix2 = {
    new Matrix2(this.iHat, this.jHat)
  }
  
  // Set the birds in local space
  def setLocalBirds(birds: Array[Bird]) = {
    def isInSight(bird: Bird) = {
      val deltaVector = bird.position - this.position
      
      val localDeltaVector = this.basisVectorMatrix.inverse * deltaVector
      val deltaX = localDeltaVector.x
      val deltaY = localDeltaVector.y
      var angle = 0.0
      
      // Minimum angle
      val minAngle = -(Math.PI / 2.0 - this.wingAngle / 2.0)
      
      if(deltaX > 0 && deltaY > 0) {
        angle = math.atan(deltaY / deltaX)
      } else if(deltaX > 0 && deltaY < 0) {
        angle = -math.atan(deltaY.abs / deltaX)
      } else if(deltaX < 0 && deltaY > 0) {
        angle = math.atan(deltaY / deltaX.abs)
      } else if(deltaX < 0 && deltaY < 0) {
        angle = -math.atan(deltaY.abs / deltaX.abs)
      } else if(deltaX == 0 && deltaY > 0) {
        angle = Math.PI / 2.0
      }
      val result = (minAngle < angle)
      result
    }
    this.localBirds = birds.filter(isInSight)
  }
  
  def drawBird(g: Graphics2D) = {
    // Vectors that represent the left and right wings of the bird.
    val a = this.iHat * -math.sin(this.wingAngle) * this.wingLength - this.jHat * math.cos(this.wingAngle) * this.wingLength
    val b = this.iHat * math.sin(this.wingAngle) * this.wingLength - this.jHat * math.cos(this.wingAngle) * this.wingLength
    
    // Global position vectors of the tips of the wings.
    val left = this.position + a
    val right = this.position + b
    
    // TODO: Draw the shape
    g.fillPolygon(Array(this.position.x, left.x, right.x).map(_.toInt), Array(this.position.y, left.y, right.y).map(_.toInt), 3)
  }
  
  var counter = 0.0
  
  def desiredVelocity = {
    // TODO: Behaviours
    this.behaviour.desiredVelocity(this.localBirds, this.position, this.velocity)
  }
  
  def steering = {
    this.desiredVelocity - this.velocity
  }
  
  def steeringAcceleration = {
    if(this.steering.mag >= Simulaatio.MAX_FORCE) {
      this.steering.unit * Simulaatio.MAX_FORCE / this.mass
    } else {
      this.steering / this.mass
    }
  }
  
  def accelerate() = {
    this.currentVelocity = this.velocity + this.steeringAcceleration
  }
  
  // Update the bird
  def update() = {
    this.accelerate()
    this.currentPosition = this.currentPosition + this.velocity
    if(this.position.x >= Simulaatio.renderDimensions._1) {
      this.currentPosition = new Vector(1, this.position.y)
    }
    if(this.position.x < 0) {
      this.currentPosition = new Vector(Simulaatio.renderDimensions._1 - 1, this.position.y)
    } 
    if(this.position.y >= Simulaatio.renderDimensions._2) {
      this.currentPosition = new Vector(this.position.x, 1)
    } 
    if(this.position.y < 0) {
      this.currentPosition = new Vector(this.position.x, Simulaatio.renderDimensions._2 - 1)
    } 
    if(this.currentVelocity.mag >= Simulaatio.MAX_SPEED) {
      this.currentVelocity = this.velocity.unit * 1
    }
    
    
  }
}