package run

import agents.EngineClass
import map.MapGenerator.Map
import mapGUI.ControlPanel

object app extends App {
  val rows = 100
  val cols = 70

  val map: Map = new Map(cols, rows)
  val Engine = new EngineClass(rows, cols, map)

  new ControlPanel()

  //val randomList = Engine.getRandom(100, 100)
  //Engine.run(randomList)
}
