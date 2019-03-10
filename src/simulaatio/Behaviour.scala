package simulaatio

class Behaviour(val cohesionFactor: Double, val alignmentFactor: Double, val evasionFactor: Double) {
  def alignment(localBirds: Array[Bird]): Vector = {
    // Compute the average velocity vectors of local birds.
    
    var averageVector = new Vector(0, 0)
    localBirds.foreach((bird) => {
      averageVector = averageVector + bird.velocity
    })
    
    if(localBirds.nonEmpty) {
      averageVector / localBirds.size
    } else {
      new Vector(0, 0)
    }
  }
  
  def cohesion(localBirds: Array[Bird], currentPosition: Vector): Vector = {
    // Compute the average position vectors of local birds.
    // Finally subtract the owner bird's position from the average.
    
    var averagePosition = new Vector(0, 0)
    localBirds.foreach((bird) => {
      averagePosition = averagePosition + bird.position
    })
    
    if(localBirds.nonEmpty) {
      averagePosition = averagePosition / localBirds.size
      averagePosition - currentPosition
    } else {
      new Vector(0, 0)
    }
  }
  
  def separation(localBirds: Array[Bird], currentPosition: Vector): Vector = {
    var totalForce = new Vector(0, 0)
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
      new Vector(0, 0)
    }
  }
  
  def desiredVelocity(localBirds: Array[Bird], currentPosition: Vector, currentVelocity: Vector): Vector = {
    val combination = this.cohesion(localBirds, currentPosition) * this.cohesionFactor + this.alignment(localBirds) * this.alignmentFactor + this.separation(localBirds, currentPosition) * this.evasionFactor
    // If no desired velocity return the current velocity.
    if(combination.x == 0 && combination.y == 0) {
      currentVelocity
    } else {
      combination
    }
  }
  
}