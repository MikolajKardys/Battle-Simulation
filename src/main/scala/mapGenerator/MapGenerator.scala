package mapGenerator

import java.util

/*
  Map generator for battle simulation.
*/

object MapGenerator extends App {
  class Cell(val squareSide: Int, val x: Int, val y: Int, val isoValue: Int, val altitude: Double) {
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

    def apply(squareSide: Int, x: Int, y: Int, isoValue: Int, altitude: Double): Cell =
      new Cell(squareSide, x, y, isoValue, altitude)
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

  class Map(val height: Int, val width: Int) {
    import mapGUI.MapGUI

    val squareSide:Int = Math.min(800/width, 800/height)
    val windowWidth:Int = squareSide * width
    val windowHeight:Int = squareSide * height

    //Cell(squareSide)
    val perlin: PerlinNoise = PerlinNoise(128)
    val biomFrequency = 0.02
    val terrainFrequency = 0.02

    //point 1 is black point 0 is white
    val points: Seq[Vector[Int]] = Vector.tabulate(height+1, width+1)((x, y) => if(perlin.noise(x*biomFrequency, y*biomFrequency) >= 0.2) 1 else 0)
    val map: Seq[Vector[Cell]] = Vector.tabulate(height, width)((x, y) =>
      Cell(squareSide, x, y, calculateIsoValue(x, y), getAltitude(perlin.noise(x*terrainFrequency, y*terrainFrequency))))

    val GUI = new MapGUI(windowWidth, windowHeight)

    addLines()
    addPolygons()
    addRectangles()
    GUI.drawMap()

    override def toString: String =
      map.map(row => row.mkString(" ")).mkString("\n")

    private def calculateIsoValue(x: Int, y: Int): Int = {
      val a = points(x)(y)
      val b = points(x)(y+1)
      val c = points(x+1)(y+1)
      val d = points(x+1)(y)
      getIsoValue(a, b, c, d)
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
        val xPix = cell.y*squareSide
        val yPix = cell.x*squareSide

        GUI.addRectangle(xPix, yPix, squareSide, squareSide, cell.altitude)
      }))
    }

  }

  object Map {
    def apply(height: Int, width: Int) = new Map(height, width)
  }

  //get random bit value
  val getRandomBit = () =>  {
    //should exist only one
    val rand = scala.util.Random
    if(rand.nextInt(100) > 50) 0 else 1
  }

  //calculate square isovalue from 16 possibilities
  val getIsoValue: (Int, Int, Int, Int) => Int = (a, b, c, d) => a*8 + b*4 + c*2 + d

  //convert noise (in range [-1,1]) to altitude
  val getAltitude = (noise: Double) => {
    (noise+1)/2
  }

  val map = Map(100, 120)
  //println(map)
}
