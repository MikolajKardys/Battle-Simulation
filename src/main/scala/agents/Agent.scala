package agents
import agents.Teams._

class Agent(var position: Vector2D, var direction: Vector2D, val team: Teams){

  // Wartości które będą zmieniały się w zależności od typu jednostki
  var statistics: Map[String, Double] = Map(
    "range" -> 1.5, "strength" -> 1, "maxHealth" -> 10, "attackCost" -> 5, "moveCost" -> 5, "maxMorale" -> 5
  )
  var health: Double = statistics("maxHealth")
  var morale: Double = statistics("maxMorale")


  var flees: Boolean = false

  var target: Agent = null
  var targetedBy: List[Agent] = List[Agent]()

  def pickTarget(enemies: List[Agent]): Unit = {
    var targets = enemies.filter(agent => agent.health > 0)
    if (targets.isEmpty){
      target = null
    }
    else {
      val proximity: Agent => Double = (other: Agent) => this.position.getDistance(other.position)
      targets = targets.sortBy(proximity)

      targetEnemy(targets.head)
    }
  }

  def targetEnemy(enemy: Agent): Unit = {
    target = enemy
    enemy.targetedBy = enemy.targetedBy.appended(this)
  }

  def die(): Unit = {
    for (agent <- targetedBy){
      agent.target = null
    }
  }

  // Blok akcji
  var tokens: Double = 0
  object ActionType extends Enumeration {
    type ActionType = Value
    val Fight, Brace, Flee = Value
  }
  import ActionType._

  def chooseAction(): ActionType = {
    if (morale < 0){
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
      chooseAction() match {
        case Fight =>
          if (!attack(enemy)) {
            move(enemy)
          }
        case Flee =>
          move(null) match {
            case true =>
            case _ => attack(enemy)
          }

        case Brace =>   //Nic nie rób; zachowujesz punkty akcji
      }
    }
  }

  def move(enemy : Agent = null): AnyVal ={
    var allMoves = Engine.getMoves(position)
    if (allMoves.nonEmpty) {
      if (enemy != null) {
        val getDist = (other: Vector2D) => enemy.position.getDistance(other)
        allMoves = allMoves.sortWith(getDist(_) < getDist(_))

        val tokenIncrease = position.getDistance(allMoves.head)
        position = allMoves.head
        tokens = statistics("moveCost") * tokenIncrease
      }
      else {
        val getDist = (other: Vector2D) => this.target.position.getDistance(other)
        allMoves = allMoves.sortWith(getDist(_) < getDist(_))

        val tokenIncrease = position.getDistance(allMoves.last)
        position = allMoves.last
        tokens = statistics("moveCost") * tokenIncrease

        if (Engine.onEdge(this)){
          flees = true
        }
        return true
      }
    }
  }

  def attack(enemy: Agent): Boolean = {
    if (position.getDistance(enemy.position) <= statistics("range")) {
      enemy.health = Math.max(0, enemy.health - statistics("strength"))

      enemy.morale -= statistics("strength")

      tokens = statistics("attackCost")
      return true
    }
    false
  }
}
