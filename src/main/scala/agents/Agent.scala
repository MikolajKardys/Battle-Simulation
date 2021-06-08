package agents

import scala.util.Random

import agents.Teams._
import utilities.Vector2D
import utilities.TerrainType._
import run.app.Engine ///WAŻNE!!!

class Agent(var position: Vector2D, var direction: Vector2D, val team: Teams) {
  object ActionType extends Enumeration {
    type ActionType = Value
    val Fight, Brace, Flee = Value
  }

  import ActionType._

  // Wartości które będą zmieniały się w zależności od typu jednostki
  var statistics: Map[String, Double] = Map()
  var terrainModifier: Map[TerrainType, Double] = Map()

  var teammates: List[Agent] = List[Agent]()
  var enemies: List[Agent] = List[Agent]()

  var hitBy: List[Agent] = List[Agent]()

  var health: Double = 0
  var lastHealth: Double = 0 // Do liczenia morale

  def criteriaVal(enemy: Agent): Double = {
    var value = 1 / position.getDistance(enemy.position)
    if (hitBy.contains(enemy)){
      value *= 4
    }
    if (health > enemy.health){
      value *= 2
    }
    value * Math.sqrt(enemy.hitBy.length + 1)
  }

  def attack(enemies: List[Agent]): Boolean = {
    var inRange = enemies.filter((agent: Agent) => position.getDistance(agent.position) <= statistics("range"))

    if (inRange.nonEmpty){
      inRange = inRange.sortBy(criteriaVal)

      val posTargets = inRange.filter((agent : Agent) => criteriaVal(inRange.last) == criteriaVal(agent))

      val target = posTargets(Random.nextInt(posTargets.length))

      val hitStrength = statistics("strength")  //TODO: Terrain advantage

      target.health -= hitStrength
      morale += target.statistics("value")

      if (target.health <= 0)
        morale += target.statistics("value")

      target.hitBy = target.hitBy.appended(this)

      val attackCost = statistics("attackCost")  //TODO: Terrain advantage

      tokens += attackCost

      return true
    }
    false
  }
  ////////////////////////////////////////////////////////////////////

  // Move block
  def move(moveType: ActionType, preCriteria: Vector2D => Double = null): Boolean ={
    var criteria = preCriteria

    if (preCriteria == null) {
      criteria = moveType match {
          case Fight => (posMove: Vector2D) => enemies.last.position.getDistance(posMove)
          case Flee => (posMove: Vector2D) => (for (enemy <- enemies) yield 1 / enemy.position.getDistance(posMove)).sum
        }
    }

    var moves = Engine.getMoves(position)

    if (moves.nonEmpty){
      moves = moves.sortBy(criteria)

      if (criteria(position) > criteria(moves.head)){
        val moveModifier = Math.pow(Engine.terrainMap.elevation(position.flip(), moves.head.flip()), 20)

        position = moves.head

        val moveCost = statistics("moveCost") * moveModifier //TODO: Terrain advantage

        tokens += moveCost

        return true
      }
    }
    false
  }

  ///  Action block  /////////////////////////////////////////////////
  var morale: Double = 0
  var flees: Boolean = false

  var tokens: Double = 0

  def calcMorale(allAgents: List[Agent]): Double = {
    var newMorale = morale

    newMorale -= (for (agent <- hitBy) yield agent.statistics("value")).sum * Math.sqrt(hitBy.length)

    val surrounding = allAgents.filter((agent: Agent) => position.getDistance(agent.position) <= 5)
    for (agent <- surrounding){
      if(agent.team == team)
        newMorale += agent.morale.sign * agent.statistics("value") / position.getDistance(agent.position)
      else {
        newMorale -= agent.morale.sign * agent.statistics("value") / position.getDistance(agent.position)
      }
    }

    Math.min(newMorale, statistics("maxMorale"))
  }

  def chooseAction(): ActionType = {  //Defaultowe wybieranie akcji;
    if (morale < 0)
      return Flee
    Fight
  }

  def doAction(canSeeFunc: (Vector2D, Vector2D) => Boolean, allAgents: List[Agent]): Unit = {
    if (tokens > 0) {
      tokens -= 1
    }
    else {
      var seeAgents = for (other <- allAgents if other.health > 0 && other != this) yield other
      seeAgents = seeAgents.filter((other: Agent) => canSeeFunc(position.flip(), other.position.flip()))

      flees = false

      enemies = List[Agent]()
      teammates = List[Agent]()
      for (agent <- seeAgents){
        if (agent.team != team)
          enemies = enemies.appended(agent)
        else
          teammates = teammates.appended(agent)
      }

      if (enemies.isEmpty)
        return

      morale = calcMorale(seeAgents)

      chooseAction() match {
        case Fight =>
          if (!attack(enemies)) {
            enemies = enemies.sortBy(criteriaVal)
            if (!move(Fight))
              brace()
          }

        case Flee =>
          if (!move(Flee)){
            val inRange = enemies.filter((agent: Agent) =>
              position.getDistance(agent.position) <= statistics("range"))

            if (inRange.isEmpty)
              brace()

            attack(inRange)
          }
          if (Engine.onEdge(this))
            flees = true

        case Brace => //Nic nie rób; zachowujesz punkty akcji
           brace()
      }

      hitBy = List[Agent]()
    }
  }
  def brace(): Unit = {

  }
}





