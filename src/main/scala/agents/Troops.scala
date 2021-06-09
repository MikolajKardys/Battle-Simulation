package agents

import agents.Teams.Teams
import run.app.Engine
import utilities.{TroopType, Vector2D}
import utilities.TerrainType.{SparseForest, _}
import utilities.TroopType._
import utilities.Vector2D.directionTab

class Infantry(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  override val troopType: TroopType = TroopType.Infantry

  statistics = Map(
    "range" -> 1.5, "strength" -> 5.0, "maxHealth" -> 20, "attackCost" -> 5, "moveCost" -> 5, "maxMorale" -> 10,
    "value" -> 1
  )

  terrainModifier = Map(
    Meadow -> 1.2, SparseForest -> 0.9, DenseForest -> 0.7, River -> 0.5
  )

  typeModifier = Map(
    TroopType.Infantry -> 1, TroopType.HeavyInfantry -> 1 / 1.2, TroopType.Cavalry -> 1 / 1.5, TroopType.Bowman -> 1
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
    "range" -> 1.5, "strength" -> 10, "maxHealth" -> 40, "attackCost" -> 5, "moveCost" -> 8, "maxMorale" -> 20,
    "value" -> 2
  )

  terrainModifier = Map(
    Meadow -> 1, SparseForest -> 0.8, DenseForest -> 0.6, River -> 0.3
  )

  typeModifier = Map(
    TroopType.Infantry -> 1.5, TroopType.HeavyInfantry -> 1, TroopType.Cavalry -> 1.5, TroopType.Bowman -> 2 / 3
  )

  health = statistics("maxHealth")
  morale = statistics("maxMorale")

  def getSide(pos: Vector2D, interval: Int): Set[Vector2D] = {
    val index = directionTab.indexOf(direction)
    Set(pos.add(directionTab((index + interval - 1) % 8)),
      pos.add(directionTab((index + interval) % 8)),
      pos.add(directionTab((index + interval + 1) % 8)))
  }
  def wontBrakeFormation(allMoves: Seq[Vector2D], newPos: Vector2D): Boolean = {
    val onRight = allMoves.count(getSide(position, 2).contains) == 3 &&
      (allMoves.count(getSide(position, 8).contains) > 0 ||
        allMoves.count(getSide(newPos, 4).contains) == 2)

    val onLeft = allMoves.count(getSide(position, 6).contains) == 3 &&
      (allMoves.count(getSide(position, 8).contains) > 0 ||
        allMoves.count(getSide(newPos, 4).contains) == 2)

    val inFront = allMoves.count(getSide(position, 8).contains) > 0 ||
      allMoves.count(getSide(newPos, 4).contains) == 3

    onRight || onLeft || inFront
  }

  //Keep formation
  override def move(moveType: ActionType.ActionType, preCriteria: Vector2D => Double): Boolean = {
    if (moveType == ActionType.Flee) {
      return super.move(moveType, null)
    }

    val moves = Engine.getMoves(position)

    direction = position.getDirection(enemies.last.position)

    val myMove = position.add(direction)

    if (moves.contains(myMove) && wontBrakeFormation(moves, myMove)){
      val moveModifier = Engine.terrainMap.elevation(position.flip(), myMove.flip())
      val terrainModifierVal = terrainModifier(Engine.terrainMap.getTerrainType(position.flip()))

      val distModifier = position.getDistance(myMove)

      position = myMove

      val moveCost = statistics("moveCost") * moveModifier * distModifier / terrainModifierVal

      tokens += moveCost

      return true
    }
    false
  }
}
object HeavyInf{
  def apply(position: Vector2D, team: Teams) = new HeavyInf(position, Vector2D(0, -1), team)
}


class Cavalry(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  override val troopType: TroopType = TroopType.Cavalry

  statistics = Map(
    "range" -> 1.5, "strength" -> 5, "maxHealth" -> 15, "attackCost" -> 5, "moveCost" -> 2, "maxMorale" -> 10,
    "value" -> 2.5
  )

  terrainModifier = Map(
    Meadow -> 1.4, SparseForest -> 0.7, DenseForest -> 0.3, River -> 0.6
  )

  typeModifier = Map(
    TroopType.Infantry -> 1.5, TroopType.HeavyInfantry -> 1 / 1.5, TroopType.Cavalry -> 1, TroopType.Bowman -> 1.5
  )

  health = statistics("maxHealth")
  morale = statistics("maxMorale")

  def keepDistance(position: Vector2D): Double ={
    Math.sqrt(Engine.getSurrounding(position, 1.5).count(_.team == team))
  }

  override def move(moveType: ActionType.ActionType, preCriteria: Vector2D => Double): Boolean = {
    var criteria: Vector2D => Double = null
    if (moveType == ActionType.Fight){
      criteria = (pos: Vector2D) =>
        enemies.last.position.getDistance(pos) * keepDistance(pos)
    }
    else {
      if (Engine.getSurrounding(position, 15).count(_.team != team) == 0)
        morale += statistics("maxMorale") * (health / statistics("maxHealth"))
    }
    super.move(moveType, criteria)
  }

}
object Cavalry{
  def apply(position: Vector2D, team: Teams) = new Cavalry(position, Vector2D(0, 0), team)
}


class Bowman(pos: Vector2D, dir: Vector2D, team: Teams) extends Agent(pos, dir, team){
  override val troopType: TroopType = TroopType.Bowman

  statistics = Map(
    "range" -> 10, "strength" -> 3, "maxHealth" -> 15, "attackCost" -> 20, "moveCost" -> 5, "maxMorale" -> 10,
    "value" -> 1.5
  )

  terrainModifier = Map(
    Meadow -> 1.2, SparseForest -> 0.9, DenseForest -> 0.7, River -> 0.5
  )

  terrainAttack = Map(
    Meadow -> 1, SparseForest -> 0.8, DenseForest -> 0.5, River -> 0.5
  )

  typeModifier = Map(
    TroopType.Infantry -> 1, TroopType.HeavyInfantry -> 1.5, TroopType.Cavalry -> 0.3, TroopType.Bowman -> 1
  )

  health = statistics("maxHealth")
  morale = statistics("maxMorale")

  //Don't charge and avoid fighting
  override def chooseAction(): ActionType.ActionType = {
    val enemiesNear = enemies.count(_.position.getDistance(position) <= 2)

    if (morale < 0 || enemiesNear > 0)
      return ActionType.Flee

    val inRange = enemies.filter(_.position.getDistance(position) <= statistics("range"))

    if (inRange.isEmpty) {
      if (position != pos){
        move(ActionType.Fight, (newPos: Vector2D) => pos.getDistance(newPos))
      }
      return ActionType.Brace
    }

    ActionType.Fight
  }
}
object Bowman{
  def apply(position: Vector2D, team: Teams) = new Bowman(position, Vector2D(0, 0), team)
}