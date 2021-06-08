package map

class Cell(val squareSide: Int, val x: Int, val y: Int,
           val sparseForestIsoValue: Int,
           val altitude: Double,
           val denseForestIsoValue: Int,
           val riverIsoValue: Int) {

  val isRiver: Boolean = if(MapConst.inTerrainIsoValue.contains(riverIsoValue)) true else false
  val isDenseForest: Boolean = if(!isRiver && MapConst.inTerrainIsoValue.contains(denseForestIsoValue)) true else false
  val isSparseForest: Boolean = if(!isDenseForest && !isRiver && MapConst.inTerrainIsoValue.contains(sparseForestIsoValue)) true else false

  val compass: Compass = Compass(squareSide)

  override def toString: String = s"$altitude"

  def getPolygons(terrain: String): (Vector[Int], Vector[Int]) = {
    val xPix = y*squareSide
    val yPix = x*squareSide
    var isoValue = 0

    if(terrain == "forest")
      isoValue = sparseForestIsoValue
    else if(terrain == "river")
      isoValue = riverIsoValue

    isoValue match {
      case 1 => (Vector(xPix, xPix + compass.xWEST, xPix + compass.xSOUTH),
        Vector(yPix + squareSide, yPix + compass.yWEST, yPix + compass.ySOUTH))
      case 2 => (Vector(xPix + squareSide, xPix + compass.xSOUTH, xPix + compass.xEAST),
        Vector(yPix + squareSide, yPix + compass.ySOUTH, yPix + compass.yEAST))
      case 3 => (Vector(xPix + compass.xWEST, xPix + compass.xEAST, xPix + squareSide, xPix),
        Vector(yPix + compass.yWEST, yPix + compass.yEAST, yPix + squareSide, yPix + squareSide))
      case 4 => (Vector(xPix + squareSide, xPix + compass.xNORTH, xPix + compass.xEAST),
        Vector(yPix, yPix + compass.yNORTH, yPix + compass.yEAST))
      case 5 => (Vector(xPix + squareSide, xPix + compass.xEAST, xPix + compass.xSOUTH, xPix, xPix + compass.xWEST, xPix + compass.xNORTH),
        Vector(yPix, yPix + compass.yEAST, yPix + compass.ySOUTH, yPix + squareSide, yPix + compass.yWEST, yPix + compass.yNORTH))
      case 6 => (Vector(xPix + squareSide, xPix + squareSide, xPix + compass.xSOUTH, xPix + compass.xNORTH),
        Vector(yPix, yPix + squareSide, yPix + compass.ySOUTH, yPix + compass.yNORTH))
      case 7 => (Vector(xPix + compass.xWEST, xPix + compass.xNORTH, xPix + squareSide, xPix + squareSide, xPix),
        Vector(yPix + compass.yWEST, yPix + compass.yNORTH, yPix, yPix + squareSide, yPix + squareSide))
      case 8 => (Vector(xPix, xPix + compass.xNORTH, xPix + compass.xWEST), Vector(yPix, yPix + compass.yNORTH, yPix + compass.yWEST))
      case 9 => (Vector(xPix + compass.xNORTH, xPix + compass.xSOUTH, xPix, xPix),
        Vector(yPix + compass.yNORTH, yPix + compass.ySOUTH, yPix + squareSide, yPix))
      case 10 => (Vector(xPix, xPix + compass.xNORTH, xPix + compass.xEAST, xPix + squareSide, xPix + compass.xSOUTH, xPix + compass.xWEST),
        Vector(yPix, yPix + compass.yNORTH, yPix + compass.yEAST, yPix + squareSide, yPix + compass.ySOUTH, yPix + compass.yWEST))
      case 11 => (Vector(xPix + compass.xNORTH, xPix + compass.xEAST, xPix + squareSide, xPix, xPix),
        Vector(yPix + compass.yNORTH, yPix + compass.yEAST, yPix + squareSide, yPix + squareSide, yPix))
      case 12 => (Vector(xPix, xPix + squareSide, xPix + compass.xEAST, xPix + compass.xWEST),
        Vector(yPix, yPix, yPix + compass.yEAST, yPix + compass.yWEST))
      case 13 => (Vector(xPix + compass.xEAST, xPix + compass.xSOUTH, xPix, xPix, xPix + squareSide),
        Vector(yPix + compass.yEAST, yPix + compass.ySOUTH, yPix + squareSide, yPix, yPix))
      case 14 => (Vector(xPix + compass.xSOUTH, xPix + compass.xWEST, xPix, xPix + squareSide, xPix + squareSide),
        Vector(yPix + compass.ySOUTH, yPix + compass.yWEST, yPix, yPix, yPix + squareSide))
      case 15 => (Vector(xPix, xPix + squareSide, xPix + squareSide, xPix),
        Vector(yPix, yPix, yPix + squareSide, yPix + squareSide))
      case _ => null
    }
  }
}

object Cell {
  def apply(squareSide: Int, x: Int, y: Int,
            sparseForestIsoValue: Int, altitude: Double, denseForestIsoValue: Int, riverIsoValue: Int): Cell =
    new Cell(squareSide, x, y, sparseForestIsoValue, altitude, denseForestIsoValue, riverIsoValue)
}
