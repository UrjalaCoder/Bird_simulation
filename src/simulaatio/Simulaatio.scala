

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
  
  val countLabel = new Label {
    text = birdCount.toString()
  }
  
  val countSlider = new Slider {
    min = 1
    max = 30
    value = birdCount
  }
  
  val startButton = new Button {
    text = "Start"
  }
  
  val stopButton = new Button {
    text = "Stop"
  }
  
  val simulationPanel = new SimulationPanel(640, 480)
  simulationPanel.visible = true
  this.contents = new BoxPanel(Orientation.Vertical) {
    this.contents += simulationPanel
    this.contents += new BoxPanel(Orientation.Horizontal) {
      this.contents += countLabel
      this.contents += countSlider
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
      this.countLabel.text = this.birdCount.toString()
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
  this.listenTo(countSlider)
  this.listenTo(startButton)
  this.listenTo(stopButton)
  
  def stop() = {
    // First set the sliders to active
    this.countSlider.enabled = true
    this.simulationRunning = false
  }
  
  // The main activation method of the simulation
  def start() = {
    this.simulationRunning = true
    this.countSlider.enabled = false
    
    this.currentGroup = Some(new Group(this.birdCount))
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