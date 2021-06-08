package agents

import agents.Teams.Teams
import run.app.Engine

import utilities.Vector2D
import utilities.TerrainType._

class Infantry(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  statistics = Map(
    "range" -> 1.5, "strength" -> 5.0, "maxHealth" -> 20, "attackCost" -> 5, "moveCost" -> 5, "maxMorale" -> 10,
    "value" -> 3
  )
  terrainModifier = Map(
    Meadow -> 1.0, SparseForest -> 1.0, DenseForest -> 1.0, River -> 1.0
  )

  health = statistics("maxHealth")
  morale = statistics("maxMorale")

  //Stay in formation
  def matesCount(pos: Vector2D):  Int = {
    val mates = Engine.getSurrounding(pos, radius = 3)
    mates.count(_.team == team)
  }
  override def move(moveType: ActionType.ActionType, preCriteria: Vector2D => Double): Boolean = {
    var criteria: Vector2D => Double = null

    super.move(moveType, criteria)
  }
}
object Infantry{
  def apply(position: Vector2D, direction: Vector2D, team: Teams) = new Infantry(position, direction, team)
}


class Bowman(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  statistics = Map(
    "range" -> 10, "strength" -> 5, "maxHealth" -> 10, "attackCost" -> 10, "moveCost" -> 5, "maxMorale" -> 10,
    "value" -> 4
  )

  health = statistics("maxHealth")
  morale = statistics("maxMorale")

  //Don't charge
  override def chooseAction(): ActionType.ActionType = {
    if (morale < 0)
      return ActionType.Flee

    val inRange = enemies.filter(_.position.getDistance(position) <= statistics("range"))

    if (inRange.isEmpty)
      return ActionType.Brace

    ActionType.Fight
  }
}
object Bowman{
  def apply(position: Vector2D, direction: Vector2D, team: Teams) = new Bowman(position, direction, team)
}

