package simulaatio

import scala.util.Random
import java.awt.Graphics2D
import scala.collection.mutable.Buffer

class Group(val groupSize: Int) {
  val random = new Random
  val localSpaceRadius = 200
  var birds: Array[Bird] = Array.fill(groupSize){
    val randomX = random.nextInt(Simulaatio.renderDimensions._1)
    val randomY = random.nextInt(Simulaatio.renderDimensions._2)
    val position = new Vector(randomX, randomY)
    val velocity = new Vector(0, 0)
    
    new Bird(position, velocity)
  }
  
  def updateSingleBird(bird: Bird) = {
    // println(bird.position)
    val otherBirds = this.birds.filter((other: Bird) => {
      other != bird
    })
    
    var neighbourBirds = otherBirds.filter((other) => {
      if(bird.distanceToBird(other) <= this.localSpaceRadius) {
        true
      } else {
        false
      }
    })
    
    
    bird.setLocalBirds(neighbourBirds.toBuffer)
    
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