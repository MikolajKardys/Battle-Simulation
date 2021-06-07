package mapGenerator

import mapGUI.Obstacles

import scala.collection.mutable

class RoadGenerator(val height: Int, val width: Int) {
  val obstaclesPerlin: PerlinNoise = PerlinNoise(128)
  val obstaclesFreq = 0.1

  val cells: Seq[Vector[(Int, Int, Int)]] = Vector.tabulate(height, width)((x, y) => if(obstaclesPerlin.noise(x*obstaclesFreq, y*obstaclesFreq) > 0.3) (x,y,1) else (x,y,0))

  val squareSide:Int = Math.min(800/width, 800/height)
  val windowWidth:Int = squareSide * width
  val windowHeight:Int = squareSide * height

  val x1 = 0
  val y1 = 0
  val x2 = height
  val y2 = width

  val obstacles = new Obstacles(windowHeight, windowWidth, squareSide)

  cells.foreach(row => row.foreach(cell => obstacles.addPoint(cell._1 * squareSide, cell._2 * squareSide, cell._3)))

  def AStarAlgorithm(x1: Int, y1: Int, x2: Int, y2: Int) = {
    val G_SCORE = 0
    val F_SCORE = 1
    val PREVIOUS = 2

    //val xs = new Array[Int](_length = agentNum)

    import mutable.HashMap
    var visited: HashMap[(Int, Int), (Int, Int, (Int, Int))] = HashMap()
    var unvisited: HashMap[(Int, Int), (Int, Int, (Int, Int))] = HashMap()

    cells.flatten.foreach(cell => unvisited.addOne(((cell._1, cell._2), (Int.MaxValue, Int.MaxValue, null))))

    //fill it
    val heurestic = 1
    unvisited.update((x1, y1), (0, heurestic, null))
  }

  obstacles.drawMap()
}
