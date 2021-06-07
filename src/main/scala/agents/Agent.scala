package agents
import agents.Teams._

import scala.util.Random

class Agent(var position: Vector2D, var direction: Vector2D, val team: Teams) {
  object ActionType extends Enumeration {
    type ActionType = Value
    val Fight, Brace, Flee = Value
  }

  import ActionType._

  // Wartości które będą zmieniały się w zależności od typu jednostki
  var statistics: Map[String, Double] = Map(
    "range" -> 1.5, "strength" -> 1.0, "maxHealth" -> 20, "attackCost" -> 5, "moveCost" -> 5, "maxMorale" -> 10,
    "value" -> 1
  )


  var teammates: List[Agent] = List[Agent]()
  var enemies: List[Agent] = List[Agent]()

  var target: Agent = null
  var hitBy: List[Agent] = List[Agent]()

  var health: Double = statistics("maxHealth")
  var lastHealth: Double = statistics("maxHealth") // Do liczenia morale

  def criteriaVal(enemy: Agent): Double = { //TODO: Zasady wyboru
    1 / position.getDistance(enemy.position)
  }

  def attack(enemies: List[Agent]): Boolean = {
    var inRange = enemies.filter((agent: Agent) => position.getDistance(agent.position) <= statistics("range"))

    if (inRange.nonEmpty){
      inRange = inRange.sortBy(criteriaVal)

      val posTargets = inRange.filter((agent : Agent) => criteriaVal(inRange.last) == criteriaVal(agent))

      target = posTargets(Random.nextInt(posTargets.length))

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
  def move(moveType: ActionType): Boolean ={
    val criteria: Vector2D => Double = moveType match {
      case Fight => (posMove: Vector2D) => enemies.last.position.getDistance(posMove)
      case Flee => (posMove: Vector2D) => (for (enemy <- enemies) yield 1 / enemy.position.getDistance(posMove)).sum
    }

    var moves = Engine.getMoves(position)

    if (moves.nonEmpty){
      moves = moves.sortBy(criteria)

      if (criteria(position) < criteria(moves.last)){
        position = moves.head

        val moveCost = statistics("moveCost")  //TODO: Terrain advantage

        tokens += moveCost

        return true
      }
    }
    false
  }

  ///  Action block  /////////////////////////////////////////////////
  var morale: Double = statistics("maxMorale")
  var flees: Boolean = false

  var tokens: Double = 0

  def calcMorale(allAgents: List[Agent]): Double = {
    var newMorale = morale

    newMorale -= (for (agent <- hitBy) yield agent.statistics("value")).sum * Math.sqrt(hitBy.length)

    val surrounding = allAgents.filter((agent: Agent) => position.getDistance(agent.position) <= 3)
    for (agent <- surrounding){
      if(agent.team == team)
        newMorale += agent.statistics("value") / position.getDistance(agent.position)
      else {
        newMorale -= agent.statistics("value") / position.getDistance(agent.position)
      }
    }

    Math.min(newMorale, statistics("maxMorale"))
  }

  def chooseAction(): ActionType = {  //Defaultowe wybieranie akcji;
    if (morale < 0)
      return Flee
    Fight
  }

  def doAction(allAgents: List[Agent]): Unit = {
    if (tokens > 0) {
      tokens -= 1
    }
    else {
      flees = false

      enemies = List[Agent]()
      teammates = List[Agent]()
      for (agent <- allAgents){
        if (agent.team != team)
          enemies = enemies.appended(agent)
        else
          teammates = teammates.appended(agent)
      }

      if (enemies.isEmpty)
        return

      morale = calcMorale(allAgents)

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





