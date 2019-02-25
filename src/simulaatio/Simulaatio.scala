

package simulaatio

import scala.swing.MainFrame
import scala.swing.Panel
import java.awt.Dimension
import scala.swing.Slider
import scala.swing.Label
import scala.swing.Button
import scala.swing.BoxPanel
import scala.swing.Orientation

class GUI(width: Int, height: Int) extends MainFrame{
  this.preferredSize = new Dimension(width, height)
  
  // Local variables used by GUI.
  var birdCount = 1
  var simulationRunning = false
  
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
  this.listenTo(countSlider)
  this.listenTo(startButton)
  this.listenTo(stopButton)
}

class SimulationPanel(width: Int, height: Int) extends Panel {
  
}

object Simulaatio extends App{
  
  def dimensions = {
    (1280, 720)
  }
  
  val gui = new GUI(dimensions._1, dimensions._2)
}