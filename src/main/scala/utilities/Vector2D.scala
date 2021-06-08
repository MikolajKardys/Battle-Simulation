package utilities

class Vector2D(val x: Int, val y: Int) {
  def dot(other: Vector2D): Double = {
    this.x*other.x + this.y*other.y
  }

  def getVector(other: Vector2D): Vector2D = {
    Vector2D(this.x - other.x, this.y - other.y)
  }

  def getDistance(other: Vector2D): Double = {
    Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y))
  }

  def getRoundDist(other: Vector2D): Int = {
    Math.round(getDistance(other)*10).asInstanceOf[Int]
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
  def apply(x: Int, y: Int): Vector2D = new Vector2D(x, y)
}