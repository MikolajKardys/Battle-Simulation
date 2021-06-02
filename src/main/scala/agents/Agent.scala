package agents
import agents.Teams._

class Agent(var position: Vector2D, var direction: Vector2D, val team: Teams) {

  // Wartości które będą zmieniały się w zależności od typu jednostki
  var statistics: Map[String, Double] = Map(
    "range" -> 1.5, "strength" -> 1.0, "maxHealth" -> 10, "attackCost" -> 5, "moveCost" -> 5, "maxMorale" -> 5,
    "value" -> 0.5
  )

  ///  Target block  /////////////////////////////////////////////////
  var target: Agent = null
  var targetedBy: List[Agent] = List[Agent]()

  def pickTarget(enemies: List[Agent]): Unit = {
    var targets = enemies.filter(agent => agent.health > 0)
    if (targets.nonEmpty) {
      val proximity: Agent => Double = (other: Agent) => position.getDistance(other.position)
      targets = targets.sortBy(proximity)

      if (target == null) {
          targetEnemy(targets.head)
      }
      else if (position.getDistance(target.position) > statistics("range")) {
        targets = targets.filter(agent => position.getDistance(agent.position) < position.getDistance(target.position))

        if (targets.nonEmpty){
          targetEnemy(targets.head)
        }
      }
    }
  }

  def targetEnemy(enemy: Agent): Unit = {
    target = enemy
    enemy.targetedBy = enemy.targetedBy.appended(this)
  }

  ////////////////////////////////////////////////////////////////////

  ///  Fighting block  ///////////////////////////////////////////////
  var health: Double = statistics("maxHealth")
  var lastHealth: Double = statistics("maxHealth") // Do liczenia morale

  def attack(enemy: Agent): Boolean = {
    val distance = position.getDistance(enemy.position)
    if (distance <= statistics("range")) {
      enemy.health -= statistics("strength") // uderzenie
      enemy.health = Math.max(0, enemy.health)

      if (enemy.health > 0) {            // counterattack agains last player that had hit us
        if (enemy.target != null){
          enemy.target.targetedBy = enemy.target.targetedBy.filter(agent => agent != enemy)
        }
        enemy.target = this
        targetedBy = targetedBy.appended(enemy)
      }

      tokens = statistics("attackCost")
      return true
    }
    false
  }

  def die(): Unit = {
    for (agent <- targetedBy){
      agent.target = null
    }
  }
  ////////////////////////////////////////////////////////////////////

  // Move block
  def move(enemy : Agent = null): Boolean ={
    var allMoves = Engine.getMoves(position)
    if (allMoves.nonEmpty) {
      val getDist = (other: Vector2D) => this.target.position.getDistance(other)
      allMoves = allMoves.sortBy(getDist(_))

      if(enemy != null && getDist(position) >= getDist(allMoves.head)){
        val getMatesNumber : Vector2D => Int = pos =>
          (for (agent <- Engine.getSurrounding(pos, 1.5) if agent.team == team) yield agent).size

        allMoves = allMoves.filter(pos => getMatesNumber(pos) > 1 || getMatesNumber(pos) >= getMatesNumber(position))

        if (allMoves.nonEmpty) {
          val tokenIncrease = position.getDistance(allMoves.head)
          position = allMoves.head
          tokens = statistics("moveCost") * tokenIncrease
          return true
        }
      }
      else if(enemy == null && getDist(position) <= getDist(allMoves.last)){
        val tokenIncrease = position.getDistance(allMoves.last)
        position = allMoves.last
        tokens = statistics("moveCost") * tokenIncrease

        if (Engine.onEdge(this))
          flees = true

        return true
      }

      return false
    }
    false
  }

  ///  Action block  /////////////////////////////////////////////////
  var morale: Double = statistics("maxMorale")
  var flees: Boolean = false

  var tokens: Double = 0

  object ActionType extends Enumeration {
    type ActionType = Value
    val Fight, Brace, Flee = Value
  }

  import ActionType._

  def calcMorale(): Double = {
    val healthDrop = lastHealth - health
    lastHealth = health
    morale -= healthDrop

    val surrounding = Engine.getSurrounding(position, 2.5)
    for (agent <- surrounding){
      if(agent.team == team)
        morale += agent.statistics("value") * agent.morale.sign
      else {
        morale -= agent.statistics("value") * agent.morale.sign
      }
    }
    Math.min(morale, statistics("maxMorale"))
  }

  def chooseAction(): ActionType = {
    if (morale < 0) {
      return Flee
    }
    Fight
  }

  def doAction(enemy: Agent): Unit = {
    if (tokens > 0) {
      tokens -= 1
    }
    else {
      flees = false
      morale = calcMorale()
      chooseAction() match {
        case Fight =>
          if (!attack(enemy))
            move(enemy)

        case Flee =>
          if (!move(null))
            attack(enemy)
          else
            morale += statistics("value") / 2

        case Brace => //Nic nie rób; zachowujesz punkty akcji
            morale += statistics("value")
      }
    }
  }
}



