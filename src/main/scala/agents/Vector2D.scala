package agents

class Vector2D(val x: Double, val y: Double) {
  def dot(other: Vector2D): Double = {
    this.x*other.x + this.y*other.y
  }
  override def equals(other: Any): Boolean = {
    other match {
      case that: Vector2D =>
        (this.x == that.x) && (this.y == that.y)
      case _ => false
    }
  }
}

object Vector2D {
  def apply(x: Double, y: Double): Vector2D = new Vector2D(x, y)
}