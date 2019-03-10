

package simulaatio

import scala.swing.MainFrame
import scala.swing.Panel
import scala.swing.event.ValueChanged
import scala.swing.event.ButtonClicked
import java.awt.Dimension
import scala.swing.Slider
import scala.swing.Label
import scala.swing.Button
import scala.swing.BoxPanel
import scala.swing.Orientation
import java.awt.Graphics2D
import java.awt.Color

class GUI(width: Int, height: Int) extends MainFrame{
  this.preferredSize = new Dimension(width, height)
  
  // Local variables used by GUI.
  var birdCount = 1
  var simulationRunning = false
  var currentGroup: Option[Group] = None
  
  var cohesionFactor = 10
  var alignmentFactor = 10
  var evasionFactor = 30
  
  val countLabel = new Label {
    text = "Bird amount: " + birdCount.toString()
  }
  
  val countSlider = new Slider {
    min = 1
    max = 50
    value = birdCount
  }
  
  // Slider for controlling the cohesionFactor
  val cohesionSlider = new Slider {
    min = 0
    max = 50
    value = cohesionFactor
  }
  
  val cohesionLabel = new Label {
    text = "Cohesion factor: " + cohesionFactor.toString()
  }
  
  val alignmentSlider = new Slider {
    min = 0
    max = 50
    value = alignmentFactor
  }
  
  val alignmentLabel = new Label {
    text = "Alignment factor: " + alignmentFactor.toString()
  }
  
  val evasionSlider = new Slider {
    min = 0
    max = 100
    value = evasionFactor
  }
  
  val evasionLabel = new Label {
    text = "Separation factor: " + evasionFactor.toString()
  }
  
  val startButton = new Button {
    text = "Start"
  }
  
  val stopButton = new Button {
    text = "Stop"
  }
  
  // Collect sliders to one array
  val sliderArray: Array[Slider] = Array(countSlider, cohesionSlider, alignmentSlider, evasionSlider)
  val simulationPanel = new SimulationPanel(640, 480)
  simulationPanel.visible = true
  this.contents = new BoxPanel(Orientation.Vertical) {
    this.contents += simulationPanel
    this.contents += new BoxPanel(Orientation.Horizontal) {
      this.contents += countLabel
      this.contents += countSlider
    }
    
    this.contents += new BoxPanel(Orientation.Horizontal) {
      this.contents += cohesionLabel
      this.contents += cohesionSlider
    }
    
    this.contents += new BoxPanel(Orientation.Horizontal) {
      this.contents += alignmentLabel
      this.contents += alignmentSlider
    }
    
    this.contents += new BoxPanel(Orientation.Horizontal) {
      this.contents += evasionLabel
      this.contents += evasionSlider
    }
    
    this.contents += new BoxPanel(Orientation.Horizontal) {
      this.contents += startButton
      this.contents += stopButton
    }
  }
  
  this.visible = true
  
  // Event handlers for the GUI elements.
  reactions += {
    case ValueChanged(`countSlider`) => {
      // Update the bird amount slider.
      this.birdCount = countSlider.value
      this.countLabel.text = "Bird amount: " + this.birdCount.toString()
    }
    
    // Update behaviour sliders
    case ValueChanged(`cohesionSlider`) => {
      this.cohesionFactor = cohesionSlider.value
      this.cohesionLabel.text = "Cohesion factor: " + this.cohesionFactor.toString()
    }
    
    case ValueChanged(`alignmentSlider`) => {
      this.alignmentFactor = alignmentSlider.value
      this.alignmentLabel.text = "Alignment factor: " + this.alignmentFactor.toString()
    }
    
    case ValueChanged(`evasionSlider`) => {
      this.evasionFactor = evasionSlider.value
      this.evasionLabel.text = "Separation factor: " + this.evasionFactor.toString()
    }
    
    case ButtonClicked(`startButton`) => {
      println("TEST")
      // Start the simulation
      this.start()
    }
    
    case ButtonClicked(`stopButton`) => {
      this.stop()
    }
  }
  this.sliderArray.foreach((slider) => {
    this.listenTo(slider)
  })
  this.listenTo(startButton)
  this.listenTo(stopButton)
  
  def stop() = {
    // First set the sliders to active
    this.sliderArray.foreach((slider) => {
      slider.enabled = true
    })
    this.simulationRunning = false
  }
  
  // The main activation method of the simulation
  def start() = {
    this.simulationRunning = true
    
    // Disable sliders
    this.sliderArray.foreach((slider) => {
      slider.enabled
    })
    
    // Scaling the values to suit the simulation.
    this.currentGroup = Some(new Group(this.birdCount, (this.cohesionFactor / 1000.0, this.alignmentFactor / 10.0, this.evasionFactor)))
    this.simulationPanel.setGroup(this.currentGroup)
    this.simulationPanel.visible = true
    val renderThread = new Thread(this.simulationPanel)
    renderThread.start()
    // Create a new update thread
    val updateThread = new Thread() {
      override def run() = {
        println("Hello!")
        var lastTime = System.nanoTime() / 1000000.0
          var one60OfSec = (1.0 / 60.0) * 1000.0
          while(simulationRunning) {
            if(System.nanoTime() / 1000000.0 - lastTime >= one60OfSec) {
          	  currentGroup.get.updateGroup()
          	  lastTime = System.nanoTime() / 1000000.0
            }
          }
          // Destroy threads when finished.
          simulationPanel.removeGroup()
          renderThread.join()
          this.join()
      }
    }
    
    // Start the update thread.
    updateThread.start()
  }
}

class SimulationPanel(width: Int, height: Int) extends Panel with Runnable {
  this.preferredSize = new Dimension(width, height)
  this.preferredSize = preferredSize
  this.maximumSize = this.preferredSize
  this.minimumSize = this.preferredSize
  
  var group: Option[Group] = None
  
  def setGroup(g: Option[Group]) = {
    this.group = g
  }
  
  def removeGroup() = {
    this.group = None
  }
  
  override def paintComponent(g: Graphics2D) = {
    g.setColor(new Color(255, 255, 255))
    g.fillRect(0, 0, this.width, this.height)
    g.setColor(new Color(0, 0, 0))
    g.translate(0, this.height)
    g.scale(1.0, -1.0)
    if(this.group.isDefined) {
    	this.group.get.drawBirds(g)
    }
  }
  
  def run() = {
    this.visible = true
    while(true) {
      this.repaint()
      this.revalidate()
    }
  }
}

object Simulaatio extends App{
  
  def dimensions = {
    (1280, 720)
  }
  
  def renderDimensions = {
    (640, 480)
  }
  
  val MAX_SPEED = 0.02
  val MAX_FORCE = 1
  
  val gui = new GUI(dimensions._1, dimensions._2)
}