package simulaatio

import scala.util.Random
import java.awt.Graphics2D

class Group(val groupSize: Int, behaviourArgs: (Double, Double, Double), birdSightRadius: Int) {
  val random = new Random
  val localSpaceRadius = birdSightRadius
  var birds: Vector[Bird] = (Array.fill(groupSize){
    val randomX = random.nextInt(Simulaatio.renderDimensions._1)
    val randomY = random.nextInt(Simulaatio.renderDimensions._2)
    val position = new Vector2(randomX, randomY)
    
    // Start with a random velocity
    val velocity = new Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1)
    
    new Bird(position, velocity, new Behaviour(this.behaviourArgs._1, this.behaviourArgs._2, this.behaviourArgs._3))
  }).toVector
  
  def updateSingleBird(bird: Bird) = {
    // First remove the bird itself.
    val otherBirds = this.birds.filter((other: Bird) => {
      other != bird
    })
    
    // Then calculate the distance.
    var neighbourBirds = otherBirds.filter((other) => {
      if(bird.distanceToBird(other) <= this.localSpaceRadius) {
        true
      } else {
        false
      }
    })
    
    
    bird.setLocalBirds(neighbourBirds)
    
    // Lastly update the bird itself.
    bird.update()
  }
  
  def updateGroup() = {
    this.birds.foreach((f) => {
      this.updateSingleBird(f)
    })
  }
  
  def drawBirds(g: Graphics2D) = {
    this.birds.foreach((bird) => {
      bird.drawBird(g)
    })
  }
  
  
  
}