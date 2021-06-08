package map
import utilities.Vector2D

trait Terrain {
  //method that return terrain type
  import utilities.TerrainType._
  def getTerrainType(position: Vector2D): TerrainType

  //method that return whether agent can see specific position
  def canSee(agentPosition: Vector2D, place: Vector2D): Boolean

  //method that return terrain elevation
  def elevation(a: Vector2D, b: Vector2D): Double

  //method to repaint map with new positions of troops
  def paintMap(xs: Array[Int], ys: Array[Int], health: Array[Double], typeA: Array[Int], morale: Array[Double])
}
