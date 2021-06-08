package agents

import agents.Teams.Teams
import run.app.Engine
import utilities.{TroopType, Vector2D}
import utilities.TerrainType._
import utilities.TroopType._

class Infantry(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  override val troopType: TroopType = TroopType.Infantry

  statistics = Map(
    "range" -> 1.5, "strength" -> 5.0, "maxHealth" -> 20, "attackCost" -> 5, "moveCost" -> 5, "maxMorale" -> 10,
    "value" -> 3
  )
  terrainModifier = Map(
    Meadow -> 1.0, SparseForest -> 1.0, DenseForest -> 1.0, River -> 1.0
  )

  health = statistics("maxHealth")
  morale = statistics("maxMorale")
}
object Infantry{
  def apply(position: Vector2D, team: Teams) = new Infantry(position, Vector2D(0, 0), team)
}


class HeavyInf(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  override val troopType: TroopType = TroopType.HeavyInfantry

  statistics = Map(
    "range" -> 10, "strength" -> 5, "maxHealth" -> 10, "attackCost" -> 10, "moveCost" -> 5, "maxMorale" -> 10,
    "value" -> 4
  )

  health = statistics("maxHealth")
  morale = statistics("maxMorale")

  //Keep loose formation
  def mateNumber(pos: Vector2D): Int = {
    val mates = Engine.getSurrounding(pos, radius = 2.25).filter(_.team == team)
    mates.length
  }

  def mateFactor(oldMateNum: Int, newMateNum: Int, scale: Double): Double = {
    if (newMateNum >= oldMateNum){
      return 0.95
    }
    1
  }

  override def move(moveType: ActionType.ActionType, preCriteria: Vector2D => Double): Boolean = {
    var criteria: Vector2D => Double = null

    val oldMateNum = mateNumber(position)

    if (moveType == ActionType.Fight)
      criteria = (posMove: Vector2D) =>
        enemies.last.position.getDistance(posMove) / enemies.last.position.getDistance(position)

    super.move(moveType, criteria)
  }
}
object HeavyInf{
  def apply(position: Vector2D, team: Teams) = new HeavyInf(position, Vector2D(0, 0), team)
}


class Cavalry(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  override val troopType: TroopType = TroopType.Cavalry

  statistics = Map(
    "range" -> 10, "strength" -> 5, "maxHealth" -> 10, "attackCost" -> 10, "moveCost" -> 5, "maxMorale" -> 10,
    "value" -> 4
  )

  health = statistics("maxHealth")
  morale = statistics("maxMorale")
}
object Cavalry{
  def apply(position: Vector2D, team: Teams) = new Cavalry(position, Vector2D(0, 0), team)
}


class Bowman(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  override val troopType: TroopType = TroopType.Bowman

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
  def apply(position: Vector2D, team: Teams) = new Bowman(position, Vector2D(0, 0), team)
}