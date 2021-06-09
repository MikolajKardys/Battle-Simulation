package utilities

import utilities.Vector2D.directionTab

class Vector2D(val x: Int, val y: Int) {
  def dot(other: Vector2D): Double = {
    this.x*other.x + this.y*other.y
  }

  def getDirection(target: Vector2D): Vector2D = {
    val vector = Vector2D(target.x - x, target.y - y)

    val angle = Math.atan2(vector.y, vector.x)

    val index: Int = (Math.floor((angle + Math.PI / 8) / (Math.PI / 4)).toInt + 8) % 8

    directionTab(index)
  }

  def add(other: Vector2D): Vector2D = {
    Vector2D(this.x + other.x, this.y + other.y)
  }

  def getVector(other: Vector2D): Vector2D = {
    Vector2D(this.x - other.x, this.y - other.y)
  }

  def getDistance(other: Vector2D): Double = {
    Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y))
  }

  def getRoundDist(other: Vector2D): Int = {
    Math.round(getDistance(other) * 10).asInstanceOf[Int]
  }

  def flip(): Vector2D = {
    Vector2D(this.y, this.x)
  }

  override def toString: String = s"($x, $y)"

  override def equals(other: Any): Boolean = {
    other match {
      case that: Vector2D =>
        (this.x == that.x) && (this.y == that.y)
      case _ => false
    }
  }

  override def hashCode(): Int = {
    y*997+x
  }
}

object Vector2D {
  val directionTab: Array[Vector2D] = Array[Vector2D](Vector2D(1, 0), Vector2D(1, 1), Vector2D(0, 1),
    Vector2D(-1, 1), Vector2D(-1, 0), Vector2D(-1, -1), Vector2D(0, -1), Vector2D(1, -1))

  def apply(x: Int, y: Int): Vector2D = new Vector2D(x, y)
}