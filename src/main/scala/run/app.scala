package run

import agents._
import map.MapGenerator.Map
import mapGUI.ControlPanel
import utilities.Vector2D


object app extends App{
  val rows = 100
  val cols = 70

  val map: Map = new Map(cols, rows)
  var Engine = new EngineClass(rows, cols, map)

  val controlPanel = new ControlPanel()

  var validV = scala.collection.mutable.Set[Vector2D]()

  while(controlPanel.wait) {Thread.sleep(100)}
  Engine.run()

  def addTroop(x: Int, y: Int): Unit = {
    if(!validV.contains(Vector2D(x,y))) {
      validV += Vector2D(x,y)
      val troopType = controlPanel.getType
      val troopTeam = controlPanel.getTeam

      import agents.Teams._
      val team = (teamInt: Int) => teamInt match {
        case 0 => Red
        case _ => Blue
      }

      troopType match {
        case 0 => Engine.agentList = Engine.agentList.appended(Infantry(Vector2D(x, y), team(troopTeam)))
        case 1 => Engine.agentList = Engine.agentList.appended(HeavyInf(Vector2D(x, y), team(troopTeam)))
        case 2 => Engine.agentList = Engine.agentList.appended(Cavalry(Vector2D(x, y), team(troopTeam)))
        case _ => Engine.agentList = Engine.agentList.appended(Bowman(Vector2D(x, y), team(troopTeam)))
      }
      Engine.repaintMap()
    }
  }
}
