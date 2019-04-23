package simulaatio

import java.awt.Graphics2D

class Bird(private var currentPosition: Vector2, initialVelocity: Vector2, private val behaviour: Behaviour) {
  private var currentVelocity = initialVelocity
  private var localBirds = Vector[Bird]()
  private val mass = 20.0
  
  // Basis Vector2s of local space iHat and jHat
  // jHat points in the direction of velocity
  private def jHat = {
   if(this.velocity.x == 0 && this.velocity.y == 0) {
     new Vector2(0, 1)
   } else {     
	   this.velocity.unit 
   }
  }
  
  // iHat always points perpendicular to the jHat.
  // iHat is always to the right of the bird's location.
  private def iHat = {
    // Cross product of j and k Vector2.
    new Vector2(this.jHat.y, -this.jHat.x)
  }
  
  def position = this.currentPosition
  def velocity = this.currentVelocity
  
  // Constants for drawing.
  private val wingAngle: Double = Math.PI / 10.0
  private val wingLength: Double = 20.0
  
  // Euclidean distance in 2D.
  def distanceToBird(other: Bird) = {
    val deltaX = math.pow(other.position.x - this.position.x, 2)
    val deltaY = math.pow(other.position.y - this.position.y, 2)
    math.sqrt(deltaX + deltaY)
  }
  
  // Local space basis Vector2 matrix
 private def basisVector2Matrix: Matrix2 = {
    new Matrix2(this.iHat, this.jHat)
  }
  
  // Set the birds in local space
  def setLocalBirds(birds: Vector[Bird]) = {
    
    // This helper method determines if the bird can "see"
    def isInSight(bird: Bird) = {
      val deltaVector2 = bird.position - this.position
      
      val localDeltaVector2 = this.basisVector2Matrix.inverse * deltaVector2
      val deltaX = localDeltaVector2.x
      val deltaY = localDeltaVector2.y
      var angle = 0.0
      
      // Minimum angle
      // The minimum angle that is used to determine the view.
      val minAngle = -(Math.PI / 2.0 - this.wingAngle / 2.0)
      
      // Fix the angle to match orientation.
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
      
      (minAngle < angle)
    }
    this.localBirds = birds.filter(isInSight)
  }
  
  def drawBird(g: Graphics2D) = {
    // Vector2s that represent the left and right wings of the bird.
    val a = this.iHat * -math.sin(this.wingAngle) * this.wingLength - this.jHat * math.cos(this.wingAngle) * this.wingLength
    val b = this.iHat * math.sin(this.wingAngle) * this.wingLength - this.jHat * math.cos(this.wingAngle) * this.wingLength
    
    // Global position Vector2s of the tips of the wings.
    val left = this.position + a
    val right = this.position + b
    
    g.fillPolygon(Array(this.position.x, left.x, right.x).map(_.toInt), Array(this.position.y, left.y, right.y).map(_.toInt), 3)
  }
  
  var counter = 0.0
  
  private def desiredVelocity = {
    this.behaviour.desiredVelocity(this.localBirds, this.position, this.velocity)
  }
  
  private def steering = {
    this.desiredVelocity - this.velocity
  }
  
  private def steeringAcceleration: Vector2 = {
    if(this.steering.mag >= Simulaatio.MAX_FORCE) {
      this.steering.unit * Simulaatio.MAX_FORCE / this.mass
    } else {
      this.steering / this.mass
    }
  }
  
  private def accelerate() = {
    this.currentVelocity = this.velocity + this.steeringAcceleration
  }
  
  // Update the bird
  def update() = {
    this.accelerate()
    this.currentPosition = this.currentPosition + this.velocity
    if(this.position.x >= Simulaatio.renderDimensions._1) {
      this.currentPosition = new Vector2(1, this.position.y)
    }
    if(this.position.x < 0) {
      this.currentPosition = new Vector2(Simulaatio.renderDimensions._1 - 1, this.position.y)
    } 
    if(this.position.y >= Simulaatio.renderDimensions._2) {
      this.currentPosition = new Vector2(this.position.x, 1)
    } 
    if(this.position.y < 0) {
      this.currentPosition = new Vector2(this.position.x, Simulaatio.renderDimensions._2 - 1)
    } 
    if(this.currentVelocity.mag >= Simulaatio.MAX_SPEED) {
      
      this.currentVelocity = this.velocity.unit * Simulaatio.MAX_SPEED
    }
    
    
  }
}