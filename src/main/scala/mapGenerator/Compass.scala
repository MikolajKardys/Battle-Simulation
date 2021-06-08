package mapGenerator

class Compass(val squareSide: Int) {
  val yNORTH: Int = 0
  val xNORTH: Int = squareSide/2
  val yEAST: Int = squareSide/2
  val xEAST: Int = squareSide
  val ySOUTH: Int = squareSide
  val xSOUTH: Int = squareSide/2
  val yWEST: Int = squareSide/2
  val xWEST: Int = 0
}

object Compass {
  var inst: Option[(Int, Compass)] = None

  def apply(_squareSide: Int): Compass = {
    inst match {
      case Some((`_squareSide`, i)) => i
      case None =>
        val i = new Compass(_squareSide)
        inst = Some((_squareSide, i))
        i
      case _ => throw new Error("Singleton initialized with different value")
    }
  }
}
