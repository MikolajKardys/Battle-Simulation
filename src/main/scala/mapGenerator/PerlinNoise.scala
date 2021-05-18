package mapGenerator

import scala.util.Random

class PerlinNoise(val N: Int) {
  class Vector2D(val x: Double, val y:Double) {
    def dot(other: Vector2D): Double = {
      this.x*other.x + this.y*other.y
    }
  }

  object Vector2D {
    def apply(x: Double, y: Double): Vector2D = new Vector2D(x, y)
  }

  val rand: Random.type = scala.util.Random
  //create permutation table
  val S: Seq[Int] = rand.shuffle((0 until N).toVector)
  val P: Seq[Int] = S ++ S

  def noise(x: Double, y: Double): Double = {
    val xIdx:Int = Math.floor(x).toInt % N
    val yIdx:Int = Math.floor(y).toInt % N
    val xOff:Double = x - Math.floor(x)
    val yOff:Double = y - Math.floor(y)

    val topRightVec = Vector2D(xOff-1.0, yOff-1.0)
    val topLeftVec = Vector2D(xOff, yOff-1.0)
    val bottomRightVec = Vector2D(xOff-1.0, yOff)
    val bottomLeftVec = Vector2D(xOff, yOff)

    val valueTopRight = P(P(xIdx+1)+yIdx+1)
    val valueTopLeft = P(P(xIdx)+yIdx+1)
    val valueBottomRight = P(P(xIdx+1)+yIdx)
    val valueBottomLeft = P(P(xIdx)+yIdx)

    val dotTopRight = topRightVec.dot(getConstantVector(valueTopRight))
    val dotTopLeft = topLeftVec.dot(getConstantVector(valueTopLeft))
    val dotBottomRight = bottomRightVec.dot(getConstantVector(valueBottomRight))
    val dotBottomLeft = bottomLeftVec.dot(getConstantVector(valueBottomLeft))

    val xOffTransformed = getFade(xOff)
    val yOffTransformed = getFade(yOff)

    val v1 = getLerp(yOffTransformed, dotBottomLeft, dotTopLeft)
    val v2 = getLerp(yOffTransformed, dotBottomRight, dotTopRight)
    getLerp(xOffTransformed, v1, v2)
  }

  val getConstantVector: Int => Vector2D = v => {
    val h = v%4
    h match {
      case 0 => Vector2D(1.0, 1.0)
      case 1 => Vector2D(-1.0, 1.0)
      case 2 => Vector2D(-1.0, -1.0)
      case 3 => Vector2D(1.0, -1.0)
      //what to do in such problem
      case _ => Vector2D(0.0, 0.0)
    }
  }

  val getFade: Double => Double = t => ((6*t-15)*t+10)*t*t*t

  val getLerp: (Double, Double, Double) => Double = (t, a, b) => a+t*(b-a)
}

object PerlinNoise {
  def apply(pointNum: Int): PerlinNoise = new PerlinNoise(pointNum)
}
