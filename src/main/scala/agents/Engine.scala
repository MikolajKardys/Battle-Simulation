package agents

import scala.util.Random
import scala.collection.mutable

object Teams extends Enumeration {
  type Teams = Value
  val Red: agents.Teams.Value = Value(1)
  val Blue: agents.Teams.Value = Value(-1)
}

import agents.Teams._

object Engine extends App {

  var agentList: List[Agent] = List[Agent]()

  def getMoves(position: Vector2D): Seq[Vector2D] = {
    val thisX = position.x
    val thisY = position.y

    val neighFields: Seq[Vector2D] =
      for (i <- (-1).to(1) if thisX + i >= 0 && thisX + i < rows;
           j <- (-1).to(1) if thisY + j >= 0 && thisY + j < cols && (i != 0 || j != 0))
          yield Vector2D(thisX + i, thisY + j)

    val fieldsTaken = for (agent <- agentList if agent.health  > 0) yield agent.position

    for (position <- neighFields if !fieldsTaken.contains(position)) yield position
  }

  def onEdge(agent: Agent): Boolean = {
    agent.position.x == 0 || agent.position.y == 0 || agent.position.x == rows - 1 || agent.position.y == cols - 1
  }

  def setupLists(reds: Int, blues: Int): mutable.Map[String, List[Agent]] = {
    var redTeam = List[Agent]()
    var blueTeam = List[Agent]()

    val redFieldList = Random.shuffle((for (i <- 0.until(rows); j <- 0.until(cols / 3)) yield (i, j)).toList).slice(0, reds)
    for (field <- redFieldList) {
      redTeam = redTeam.appended(Infantry(Vector2D(field._1, field._2), Vector2D(0, 0), Red))
    }

    val blueFieldList = Random.shuffle((for (i <- 0.until(rows); j <- (2 * cols / 3).until(cols))
      yield (i, j)).toList).slice(0, blues)
    for (field <- blueFieldList) {
      blueTeam = blueTeam.appended(Cavalry(Vector2D(field._1, field._2), Vector2D(0, 0), Blue))
    }

    agentList = agentList.appendedAll(redTeam).appendedAll(blueTeam)

    mutable.Map("reds" -> redTeam, "blues" -> blueTeam)
  }

  def run(reds: Int, blues: Int): Unit = {
    repaintMap()

    val teams = setupLists(reds, blues)

    for (_ <- 1.to(1000)){
      for (agent <- agentList)
        if (agent.flees)
          agent.die()
      agentList = agentList.filter(agent => !agent.flees)
      teams("reds") = teams("reds").filter(agent => !agent.flees)
      teams("blues") = teams("blues").filter(agent => !agent.flees)

      agentList = Random.shuffle(agentList)

      for (agent <- agentList){
        if (agent.health  > 0) {
          var enemies: List[Agent] = null
          agent.team match {
            case Red => enemies = teams("blues")
            case Blue => enemies = teams("reds")
          }

          if (enemies.isEmpty) {
            repaintMap()
            println(s"${agent.team.toString} wygrywa!")
            return
          }

          agent.pickTarget(enemies)
          if (agent.target == null){
            repaintMap()
            println(s"${agent.team.toString} wygrywa!")
            return
          }

          agent.doAction(agent.target)
        }
      }

      // Remove the dead
      for (agent <- agentList)
        if (agent.health == 0)
          agent.die()
      agentList = agentList.filter(agent => agent.health  > 0)
      teams("reds") = teams("reds").filter(agent => agent.health  > 0)
      teams("blues") = teams("blues").filter(agent => agent.health  > 0)


      //Handle visualization
      repaintMap()
      Thread.sleep(10)
    }
  }

  def repaintMap(): Unit = {
    val agentNum = agentList.length

    val xs = new Array[Int](_length = agentNum)
    val ys = new Array[Int](_length = agentNum)
    val color = new Array[Double](_length = agentNum)
    val morale = new Array[Double](_length = agentNum)

    var index = 0
    for (agent <- agentList){
      xs(index) = agent.position.x
      ys(index) = agent.position.y
      color(index) = agent.team.id * agent.health / agent.statistics("maxHealth")
      morale(index) = Math.min(1, Math.max(0, agent.morale / agent.statistics("maxMorale")))

      index += 1
    }

    canvas.repaint(xs, ys, color, morale)
  }

  val rows = 30
  val cols = 60
  import tmp_viz.Main
  val canvas = new Main(rows, cols)
  run(50, 2)
}