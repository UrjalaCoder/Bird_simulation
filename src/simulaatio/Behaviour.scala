package simulaatio

class Behaviour(val cohesionFactor: Double, val alignmentFactor: Double, val evasionFactor: Double) {
  private def alignment(localBirds: Array[Bird]): Vector2 = {
    // Compute the average velocity Vector2s of local birds.
    
    var averageVector2 = new Vector2(0, 0)
    localBirds.foreach((bird) => {
      averageVector2 = averageVector2 + bird.velocity
    })
    
    if(localBirds.nonEmpty) {
      averageVector2 / localBirds.size
    } else {
      new Vector2(0, 0)
    }
  }
  
  private def cohesion(localBirds: Array[Bird], currentPosition: Vector2): Vector2 = {
    // Compute the average position Vector2s of local birds.
    // Finally subtract the owner bird's position from the average.
    
    var averagePosition = new Vector2(0, 0)
    localBirds.foreach((bird) => {
      averagePosition = averagePosition + bird.position
    })
    
    if(localBirds.nonEmpty) {
      averagePosition = averagePosition / localBirds.size
      averagePosition - currentPosition
    } else {
      new Vector2(0, 0)
    }
  }
  
  private def separation(localBirds: Array[Bird], currentPosition: Vector2): Vector2 = {
    var totalForce = new Vector2(0, 0)
    localBirds.foreach((bird) => {
      val deltaPosition = currentPosition - bird.position
      val distance = deltaPosition.mag
      if(distance != 0) {        
    	  totalForce = totalForce + deltaPosition.unit * (1.0 / distance)
      }
    })
    
    if(localBirds.nonEmpty) {
      totalForce
    } else {
      new Vector2(0, 0)
    }
  }
  
  def desiredVelocity(localBirds: Array[Bird], currentPosition: Vector2, currentVelocity: Vector2): Vector2 = {
    val combination = this.cohesion(localBirds, currentPosition) * this.cohesionFactor + this.alignment(localBirds) * this.alignmentFactor + this.separation(localBirds, currentPosition) * this.evasionFactor
    // println((this.separation(localBirds, currentPosition) * evasionFactor).mag)
    // If no desired velocity return the current velocity.
    if(combination.x == 0 && combination.y == 0) {
      currentVelocity
    } else {
      combination
    }
  }
  
}