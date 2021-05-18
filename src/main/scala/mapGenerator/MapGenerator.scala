package mapGenerator

/*
  Map generator for battle simulation.
*/

object MapGenerator extends App {
  class Cell(val squareSide: Int, val x: Int, val y: Int, val isoValue: Int) {
    val yNORTH = 0
    val xNORTH = squareSide/2
    val yEAST = squareSide/2
    val xEAST = squareSide
    val ySOUTH = squareSide
    val xSOUTH = squareSide/2
    val yWEST = squareSide/2
    val xWEST = 0

    override def toString(): String = s"$isoValue"

    def getLines() = {
      val xPix = y*squareSide
      val yPix = x*squareSide

      isoValue match {
        case 1 => (xPix + xWEST, yPix + yWEST, xPix + xSOUTH, yPix + ySOUTH)
        case 2 => (xPix + xSOUTH, yPix + ySOUTH, xPix + xEAST, yPix + yEAST)
        case 3 | 12 => (xPix + xEAST, yPix + yEAST, xPix + xWEST, yPix + yWEST)
        case 4 | 11 => (xPix + xNORTH, yPix + yNORTH, xPix + xEAST, yPix + yEAST)
        case 5 => ((xPix + xNORTH, yPix + yNORTH, xPix + xWEST, yPix + yWEST), (xPix + xSOUTH, yPix + ySOUTH, xPix + xEAST, yPix + yEAST))
        case 6 | 9 => (xPix + xNORTH, yPix + yNORTH, xPix + xSOUTH, yPix + ySOUTH)
        case 7 | 8 => (xPix + xNORTH, yPix + yNORTH, xPix + xWEST, yPix + yWEST)
        case 10 => ((xPix + xNORTH, yPix + yNORTH, xPix + xEAST, yPix + yEAST), (xPix + xSOUTH, yPix + ySOUTH, xPix + xWEST, yPix + yWEST))
        case 13 => (xPix + xEAST, yPix + yEAST, xPix + xSOUTH, yPix + ySOUTH)
        case 14 => (xPix + xWEST, yPix + yWEST, xPix + xSOUTH, yPix + ySOUTH)
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

    def apply(squareSide: Int, x: Int, y: Int, isoValue: Int): Cell = new Cell(squareSide, x, y, isoValue)
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
    val perlin = PerlinNoise(256)
    val frequency = 0.03
    val thresh = 0.5

    val points = Vector.tabulate(height+1, width+1)((x, y) =>
      if(perlin.noise(x*frequency, y*frequency) >= thresh) 1 else 0)
    //val points = Vector.tabulate(height+1, width+1)((x, y) => getRandomBit())
    val map = Vector.tabulate(height, width)((x, y) => Cell(squareSide, x, y, calculateIsoValue(x, y)))

    val GUI = new MapGUI(windowWidth, windowHeight)

    map.foreach(row => row.foreach(cell => {
      val lines = cell.getLines()
      lines match {
        case (x1:Int, y1:Int, x2:Int, y2:Int) => {
          GUI.addLine(x1, y1, x2, y2)
        }
        case ((x1:Int, y1:Int, x2:Int, y2:Int), (x3:Int, y3:Int, x4:Int, y4:Int)) => {
          GUI.addLine(x1, y1, x2, y2)
          GUI.addLine(x3, y3, x4, y4)
        }
        case _ =>
      }
    }))

    GUI.drawMap()

    override def toString(): String =
      map.map(row => row.mkString(" ")).mkString("\n")

    private def calculateIsoValue(x: Int, y: Int): Int = {
      val a = points(x)(y)
      val b = points(x)(y+1)
      val c = points(x+1)(y+1)
      val d = points(x+1)(y)
      getIsoValue(a, b, c, d)
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

  val map = Map(50, 50)
  //println(map)
}
