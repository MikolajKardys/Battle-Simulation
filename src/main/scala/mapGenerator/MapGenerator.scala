package mapGenerator

import utilities.Vector2D

import java.util

/*
  Map generator for battle simulation.
*/

object MapGenerator{
  class Cell(val squareSide: Int, val x: Int, val y: Int, val isoValue: Int, val altitude: Double, var denseForestIsoValue: Int) {
    val isDenseForest: Boolean = if(MapConst.inForestIsoValue.contains(denseForestIsoValue)) true else false
    val isSparseForest: Boolean = if(!isDenseForest && MapConst.inForestIsoValue.contains(isoValue)) true else false
    val isRiver: Boolean = true

    val yNORTH = 0
    val xNORTH: Int = squareSide/2
    val yEAST: Int = squareSide/2
    val xEAST: Int = squareSide
    val ySOUTH: Int = squareSide
    val xSOUTH: Int = squareSide/2
    val yWEST: Int = squareSide/2
    val xWEST = 0

    override def toString: String = s"$altitude"

    def getLines: Serializable = {
      //do not change next two lines!
      val xPix = y*squareSide
      val yPix = x*squareSide

      isoValue match {
        case 1 => (xPix + xWEST, yPix + yWEST, xPix + xSOUTH, yPix + ySOUTH)
        case 2 => (xPix + xSOUTH, yPix + ySOUTH, xPix + xEAST, yPix + yEAST)
        case 3 | 12 => (xPix + xEAST, yPix + yEAST, xPix + xWEST, yPix + yWEST)
        case 4 | 11 => (xPix + xNORTH, yPix + yNORTH, xPix + xEAST, yPix + yEAST)
        case 5  => ((xPix + xNORTH, yPix + yNORTH, xPix + xWEST, yPix + yWEST), (xPix + xSOUTH, yPix + ySOUTH, xPix + xEAST, yPix + yEAST))
        case 6 | 9 => (xPix + xNORTH, yPix + yNORTH, xPix + xSOUTH, yPix + ySOUTH)
        case 7 | 8 => (xPix + xNORTH, yPix + yNORTH, xPix + xWEST, yPix + yWEST)
        case 10 => ((xPix + xNORTH, yPix + yNORTH, xPix + xEAST, yPix + yEAST), (xPix + xSOUTH, yPix + ySOUTH, xPix + xWEST, yPix + yWEST))
        case 13 => (xPix + xEAST, yPix + yEAST, xPix + xSOUTH, yPix + ySOUTH)
        case 14 => (xPix + xWEST, yPix + yWEST, xPix + xSOUTH, yPix + ySOUTH)
        case _ => null
      }
    }

    def getPolygons: (Vector[Int], Vector[Int]) = {
      val xPix = y*squareSide
      val yPix = x*squareSide

      isoValue match {
        case 1 => (Vector(xPix, xPix + xWEST, xPix + xSOUTH), Vector(yPix + squareSide, yPix + yWEST, yPix + ySOUTH))
        case 2 => (Vector(xPix + squareSide, xPix + xSOUTH, xPix + xEAST),
          Vector(yPix + squareSide, yPix + ySOUTH, yPix + yEAST))
        case 3 => (Vector(xPix + xWEST, xPix + xEAST, xPix + squareSide, xPix),
          Vector(yPix + yWEST, yPix + yEAST, yPix + squareSide, yPix + squareSide))
        case 4 => (Vector(xPix + squareSide, xPix + xNORTH, xPix + xEAST), Vector(yPix, yPix + yNORTH, yPix + yEAST))
        case 5 => (Vector(xPix + squareSide, xPix + xEAST, xPix + xSOUTH, xPix, xPix + xWEST, xPix + xNORTH),
          Vector(yPix, yPix + yEAST, yPix + ySOUTH, yPix + squareSide, yPix + yWEST, yPix + yNORTH))
        case 6 => (Vector(xPix + squareSide, xPix + squareSide, xPix + xSOUTH, xPix + xNORTH),
          Vector(yPix, yPix + squareSide, yPix + ySOUTH, yPix + yNORTH))
        case 7 => (Vector(xPix + xWEST, xPix + xNORTH, xPix + squareSide, xPix + squareSide, xPix),
          Vector(yPix + yWEST, yPix + yNORTH, yPix, yPix + squareSide, yPix + squareSide))
        case 8 => (Vector(xPix, xPix + xNORTH, xPix + xWEST), Vector(yPix, yPix + yNORTH, yPix + yWEST))
        case 9 => (Vector(xPix + xNORTH, xPix + xSOUTH, xPix, xPix),
          Vector(yPix + yNORTH, yPix + ySOUTH, yPix + squareSide, yPix))
        case 10 => (Vector(xPix, xPix + xNORTH, xPix + xEAST, xPix + squareSide, xPix + xSOUTH, xPix + xWEST),
          Vector(yPix, yPix + yNORTH, yPix + yEAST, yPix + squareSide, yPix + ySOUTH, yPix + yWEST))
        case 11 => (Vector(xPix + xNORTH, xPix + xEAST, xPix + squareSide, xPix, xPix),
          Vector(yPix + yNORTH, yPix + yEAST, yPix + squareSide, yPix + squareSide, yPix))
        case 12 => (Vector(xPix, xPix + squareSide, xPix + xEAST, xPix + xWEST),
          Vector(yPix, yPix, yPix + yEAST, yPix + yWEST))
        case 13 => (Vector(xPix + xEAST, xPix + xSOUTH, xPix, xPix, xPix + squareSide),
          Vector(yPix + yEAST, yPix + ySOUTH, yPix + squareSide, yPix, yPix))
        case 14 => (Vector(xPix + xSOUTH, xPix + xWEST, xPix, xPix + squareSide, xPix + squareSide),
          Vector(yPix + ySOUTH, yPix + yWEST, yPix, yPix, yPix + squareSide))
        case 15 => (Vector(xPix, xPix + squareSide, xPix + squareSide, xPix),
          Vector(yPix, yPix, yPix + squareSide, yPix + squareSide))
        case _ => null
      }
    }
  }

  object Cell {
    /*var squareSide = None: Option[Int]

    var yNORTH = None: Option[Int]
    var xNORTH = None: Option[Int]
    var yEAST = None: Option[Int]
    var xEAST = None: Option[Int]
    var ySOUTH = None: Option[Int]
    var xSOUTH = None: Option[Int]
    var yWEST = None: Option[Int]
    var xWEST = None: Option[Int]*/

    def apply(squareSide: Int, x: Int, y: Int, isoValue: Int, altitude: Double, denseForestIsoValue: Int): Cell =
      new Cell(squareSide, x, y, isoValue, altitude, denseForestIsoValue)
    /*def apply(_squareSide: Int): Unit = {
      squareSide = Some(_squareSide)

      yNORTH = Some(0:Int)
      xNORTH = Some(_squareSide/2:Int)
      yEAST = Some(_squareSide/2:Int)
      xEAST = Some(_squareSide:Int)
      ySOUTH = Some(_squareSide:Int)
      xSOUTH = Some(_squareSide/2:Int)
      yWEST = Some(_squareSide/2:Int)
      xWEST = Some(0:Int)
    }*/
  }

  object MapConst {
    val inForestIsoValue: Array[Int] = Array(3, 5, 6, 7, 9, 10, 11, 12, 13, 14 ,15)

    val biomFrequency = 0.02
    val terrainFrequency = 0.02
    val sparseForestThresh = 0.3
    val denseForestThresh: Double = sparseForestThresh + 0.1
  }

  class Map(val height: Int, val width: Int) extends Terrain {
    import mapGUI.MapGUI

    val squareSide:Int = Math.min(800/width, 800/height)
    val windowWidth:Int = squareSide * width
    val windowHeight:Int = squareSide * height

    //Cell(squareSide)
    val biomPerlin: PerlinNoise = PerlinNoise(128)
    val terrainPerlin: PerlinNoise = PerlinNoise(128)

    //point 1 is black point 0 is white
    val points: Seq[Vector[Double]] = Vector.tabulate(height+1, width+1)((x, y) => biomPerlin.noise(x*MapConst.biomFrequency, y*MapConst.biomFrequency))

    val map: Seq[Vector[Cell]] = Vector.tabulate(height, width)((x, y) =>
      Cell(squareSide, x, y, calculateIsoValue(x, y, MapConst.sparseForestThresh),
        getAltitude(terrainPerlin.noise(x*MapConst.terrainFrequency, y*MapConst.terrainFrequency)),
        calculateIsoValue(x, y, MapConst.denseForestThresh)))

    val GUI = new MapGUI(windowWidth, windowHeight, squareSide)

    addPoints()
    addLines()
    addPolygons()
    addRectangles()
    GUI.drawMap()

    override def toString: String =
      map.map(row => row.mkString(" ")).mkString("\n")

    private def calculateIsoValue(x: Int, y: Int, thresh: Double): Int = {
      val a = if(points(x)(y) >= thresh) 1 else 0
      val b = if(points(x)(y+1) >= thresh) 1 else 0
      val c = if(points(x+1)(y+1) >= thresh) 1 else 0
      val d = if(points(x+1)(y) >= thresh) 1 else 0
      getIsoValue(a, b, c, d)
    }

    private def addPoints(): Unit = {
      map.foreach(row => row.foreach(cell => {
        if(cell.isDenseForest)
          GUI.addPoint(cell.y * squareSide, cell.x * squareSide, 2)
        else if(cell.isSparseForest)
          GUI.addPoint(cell.y * squareSide, cell.x * squareSide, 1)
        else
          GUI.addPoint(cell.y * squareSide, cell.x * squareSide, 0)
      }))
    }

    private def addLines(): Unit = {
      map.foreach(row => row.foreach(cell => {
        val lines = cell.getLines
        lines match {
          case (x1:Int, y1:Int, x2:Int, y2:Int) =>
            GUI.addLine(x1, y1, x2, y2)

          case ((x1:Int, y1:Int, x2:Int, y2:Int), (x3:Int, y3:Int, x4:Int, y4:Int)) =>
            GUI.addLine(x1, y1, x2, y2)
            GUI.addLine(x3, y3, x4, y4)

          case _ =>
        }
      }))
    }

    private def addPolygons():Unit = {
      map.foreach(row => row.foreach(cell => {
        val polygons = cell.getPolygons

        polygons match {
          case (xs: Vector[Int], ys: Vector[Int]) =>
            val xsJava = new util.Vector[Integer]()
            val ysJava = new util.Vector[Integer]()

            xs.foreach(x => xsJava.add(x))
            ys.foreach(y => ysJava.add(y))

            GUI.addPolygon(xsJava, ysJava)
          case _ =>
        }
      }))
    }

    private def addRectangles():Unit = {
      map.foreach(row => row.foreach(cell => {
        val xPix = cell.y * squareSide
        val yPix = cell.x * squareSide

        GUI.addRectangle(xPix, yPix, squareSide, squareSide, cell.altitude)
      }))
    }

    import utilities.TerrainType._
    override def getTerrainType(position: Vector2D): TerrainType = {
      if(map(position.x)(position.y).isSparseForest)
        SparseForest
      else if(map(position.x)(position.y).isSparseForest)
        DenseForest
      else
        Meadow
    }

    override def canSee(agentPosition: Vector2D, place: Vector2D): Boolean = {
      val visionLine: Array[(Int, Int)] = getVisionLine(agentPosition.x, place.x, agentPosition.y, place.y)

      var visibility: Double = 0
      visionLine.foreach{
        case (x: Int, y: Int) =>
          if (map(x)(y).isDenseForest) visibility -= 2
          else if (map(x)(y).isSparseForest) visibility -= 1
        case _ =>
      }

      if(map(agentPosition.x)(agentPosition.y).isSparseForest && (!map(place.x)(place.y).isSparseForest && !map(place.x)(place.y).isDenseForest)) {
        //agent is in sparse forest and place is outside of forest
        if(visibility >= -5)
          true
        else
          false
      }
      else if(!map(agentPosition.x)(agentPosition.y).isSparseForest && !map(agentPosition.x)(agentPosition.y).isDenseForest && (map(place.x)(place.y).isSparseForest || map(place.x)(place.y).isDenseForest)) {
        //agent is outside of the forest and place is in the forest
        if(visibility >= -1)
          true
        else
          false
      }
      else {
        //every other possibility
        if(visibility >= -10)
          true
        else
          false
      }
    }

    override def elevation(a: Vector2D, b: Vector2D): Double = {
      val tgTheta: Double = (map(a.x)(a.y).altitude - map(b.x)(b.y).altitude)/a.getDistance(b)
      if(tgTheta <= 1 && tgTheta >= -1)
        Math.exp(tgTheta)
      else if(tgTheta > 1)
        Math.E
      else
        1/Math.E
    }

    override def paintMap(xs: Array[Int], ys: Array[Int], health: Array[Double], typeA: Array[Int], morale: Array[Double]): Unit = {
      GUI.paintMap(xs, ys, health, typeA, morale)
    }
  }

  object Map {
    def apply(height: Int, width: Int) = new Map(height, width)
  }

  //get random bit value
  val getRandomBit: () => Int = () =>  {
    //should exist only one
    val rand = scala.util.Random
    if(rand.nextInt(100) > 50) 0 else 1
  }

  //calculate square isovalue from 16 possibilities
  val getIsoValue: (Int, Int, Int, Int) => Int = (a, b, c, d) => a*8 + b*4 + c*2 + d

  //convert noise (in range [-1,1]) to altitude
  val getAltitude: Double => Double = (noise: Double) => {
    (noise+1)/2
  }

  val getVisionLine: (Int, Int, Int, Int) => Array[(Int, Int)] = (xAgent, xPlace, yAgent, yPlace) => {
    var kx: Int = 0
    var ky: Int = 0
    var dx: Int = 0
    var dy: Int = 0
    var err: Double = .0
    var x1: Int = xAgent
    val x2: Int = xPlace
    var y1: Int = yAgent
    val y2: Int = yPlace
    var line: Array[(Int, Int)] = Array((x1, y1))
    kx = if (x1 <= x2) 1 else -1
    ky = if (y1 <= y2) 1 else -1
    dx = Math.abs(x1 - x2)
    dy = Math.abs(y1 - y2)

    if(dx >= dy) {
      err = dx.toDouble / 2
      for(_ <- 0 until dx - 1) {
        x1 += kx
        err -= dy
        if (err < 0) {
          y1 += ky
          err += dx
        }
        line = line :+ (x1, y1)
      }
    }
    else {
      err = dy.toDouble / 2
      for (_ <- 0 until dy - 1) {
        y1 += ky
        err -= dx
        if (err < 0) {
          x1 += kx
          err += dy
        }
        line = line :+ (x1, y1)
      }
    }
    line = line :+ (x2, y2)
    line
  }
}
