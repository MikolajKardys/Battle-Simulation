package agents

import scala.util.Random
import agents.Teams._
import utilities.Vector2D
import utilities.TerrainType._
import run.app.Engine ///WAŻNE!!!
import utilities.TroopType._

class Agent(var position: Vector2D, var direction: Vector2D, val team: Teams) {
  val troopType: TroopType = None

  object ActionType extends Enumeration {
    type ActionType = Value
    val Fight, Brace, Flee = Value
  }

  import ActionType._

  // Wartości zależące od typu jednostki
  var statistics: Map[String, Double] = Map()
  var terrainModifier: Map[TerrainType, Double] = Map()
  var terrainAttack: TerrainType => Double = Map(
    Meadow -> 1, SparseForest -> 1, DenseForest -> 1, River -> 1
  )
  var typeModifier: Map[TroopType, Double] = Map()
  /////////////////////////////////////////

  var teammates: List[Agent] = List[Agent]()
  var enemies: List[Agent] = List[Agent]()

  var hitBy: List[Agent] = List[Agent]()

  var health: Double = 0
  var lastHealth: Double = 0 // Do liczenia morale

  def die(): Unit = {
    val mates = Engine.getSurrounding(position, radius = 3).filter(_.team == team)

    for (mate <- mates){
      mate.morale -= statistics("value")
    }
  }

  def criteriaVal(enemy: Agent): Double = {
    var value = 1 / position.getDistance(enemy.position)
    if (hitBy.contains(enemy)){
      value *= 4
    }
    if (health > enemy.health){
      value *= 2
    }
    value * Math.sqrt(enemy.hitBy.length + 1) * typeModifier(enemy.troopType)
  }

  def attack(enemies: List[Agent]): Boolean = {
    var inRange = enemies.filter((agent: Agent) => position.getDistance(agent.position) <= statistics("range"))

    if (inRange.nonEmpty){
      inRange = inRange.sortBy(criteriaVal)

      val posTargets = inRange.filter((agent : Agent) => criteriaVal(inRange.last) == criteriaVal(agent))

      val target = posTargets(Random.nextInt(posTargets.length))

      val currTerrain = terrainAttack(Engine.terrainMap.getTerrainType(position.flip()))

      val hitStrength = statistics("strength") * currTerrain

      target.health -= hitStrength * typeModifier(target.troopType)

      morale += target.statistics("value") * typeModifier(target.troopType)

      if (target.health <= 0)
        morale += target.statistics("value")

      target.hitBy = target.hitBy.appended(this)

      val attackCost = statistics("attackCost") * currTerrain
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
        val moveModifier = Engine.terrainMap.elevation(position.flip(), moves.head.flip())

        val distModifier = position.getDistance(moves.head)

        val moveCost = statistics("moveCost") * moveModifier * distModifier /
          terrainModifier(Engine.terrainMap.getTerrainType(position.flip()))

        position = moves.head

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
        newMorale -= agent.morale.sign * agent.statistics("value") / position.getDistance(agent.position) /
          typeModifier(agent.troopType)
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
      //Nie należy zmieniać tego var na val, ponieważ zakomentowany niżej kod pozwala na zastosowanie
      //algorytmu ograniczającego pole widzenia jednostek na polu bitwy; to tego służy też nieużywana
      //obecnie metoda 'canSeeFunc'
      var seeAgents = for (other <- allAgents if other.health > 0 && other != this) yield other
      //seeAgents = seeAgents.filter((other: Agent) => canSeeFunc(position.flip(), other.position.flip()))


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
    morale += statistics("maxMorale") / 10
  }
}





