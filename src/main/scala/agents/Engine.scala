package agents

import utilities.Vector2D

import scala.util.Random
import scala.collection.mutable

object Teams extends Enumeration {
  type Teams = Value
  val Red: agents.Teams.Value = Value(1)
  val Blue: agents.Teams.Value = Value(-1)
}

import agents.Teams._
import map.MapGenerator.Map

class EngineClass(val rows: Int, val cols: Int, val terrainMap: Map){
  var agentList: List[Agent] = List[Agent]()

  def getFields(position: Vector2D): Seq[Vector2D] = {
    val thisX = position.x
    val thisY = position.y

    for (i <- (-1).to(1) if thisX + i >= 0 && thisX + i < rows;
           j <- (-1).to(1) if thisY + j >= 0 && thisY + j < cols && (i != 0 || j != 0))
        yield Vector2D(thisX + i, thisY + j)
  }

  def getMoves(position: Vector2D): Seq[Vector2D] = {
    val neighFields = getFields(position)

    val fieldsTaken = for (agent <- agentList if agent.health  > 0) yield agent.position

    for (position <- neighFields if !fieldsTaken.contains(position)) yield position
  }

  def getSurrounding(position: Vector2D, radius: Double): List[Agent] = {
    for (agent <- agentList if agent.health > 0 && agent.position.getDistance(position) <= radius) yield agent
  }

  def onEdge(agent: Agent): Boolean = {
    agent.position.x == 0 || agent.position.y == 0 || agent.position.x == rows - 1 || agent.position.y == cols - 1
  }

  def getRandom(reds: Int, blues: Int): List[Agent] = {
    var redTeam = List[Agent]()
    var blueTeam = List[Agent]()

    val redFieldList = Random.shuffle((for (i <- 0.until(rows); j <- 0.to(2)) yield (i, j)).toList).slice(0, reds)
    for (field <- redFieldList) {
      redTeam = redTeam.appended(Infantry(Vector2D(field._1, field._2), Red))
    }

    val blueFieldList = Random.shuffle((for (i <- 0.until(rows); j <- (2 * cols / 3).until(2 * cols / 3 + 2))
      yield (i, j)).toList).slice(0, blues)
    for (field <- blueFieldList) {
      blueTeam = blueTeam.appended(Infantry(Vector2D(field._1, field._2), Blue))
    }

    agentList.appendedAll(redTeam).appendedAll(blueTeam)
  }

  def setupLists(): mutable.Map[String, List[Agent]] = {
    var redTeam = List[Agent]()
    var blueTeam = List[Agent]()

    for (agent <- agentList){
      if (agent.team == Red)
        redTeam = redTeam.appended(agent)
      else
        blueTeam = blueTeam.appended(agent)
    }

    mutable.Map("reds" -> redTeam, "blues" -> blueTeam)
  }

  def run(): Unit = {
    val teams = setupLists()

    repaintMap()

    for (_ <- 1 to 2000){
      if (teams("blues").isEmpty) {
        println("Czerwoni wygrywają!")
        repaintMap()
        return
      }
      else if (teams("reds").isEmpty) {
        println("Niebiescy wygrywają!")
        repaintMap()
        return
      }

      agentList = Random.shuffle(agentList)
      for (agent <- agentList){                         //Iteracja dla każdego agenta, który jeszcze żyje
        if (agent.health > 0) {
          agent.doAction(terrainMap.canSee, agentList)
        }
      }

      agentList = agentList.filter(agent => agent.health > 0 && !agent.flees)
      teams("reds") = teams("reds").filter(agent => agent.health > 0 && !agent.flees)
      teams("blues") = teams("blues").filter(agent => agent.health > 0 && !agent.flees)

      //Handle visualization
      Thread.sleep(10)
      repaintMap()
    }
  }

  def repaintMap(): Unit = {
    val agentNum = agentList.length

    val xs = new Array[Int](_length = agentNum)
    val ys = new Array[Int](_length = agentNum)
    val color = new Array[Double](_length = agentNum)
    val morale = new Array[Double](_length = agentNum)
    val troopType = new Array[Int](_length = agentNum)

    var index = 0
    for (agent <- agentList){
      xs(index) = agent.position.x
      ys(index) = agent.position.y
      color(index) = agent.team.id * agent.health / agent.statistics("maxHealth")
      morale(index) = Math.min(1, Math.max(0, agent.morale / agent.statistics("maxMorale")))
      troopType(index) = 0

      index += 1
    }

    terrainMap.paintMap(xs, ys, color, troopType, morale)
  }
}