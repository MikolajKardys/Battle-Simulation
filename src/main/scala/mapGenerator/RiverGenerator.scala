package mapGenerator

import utilities.Vector2D

import scala.collection.mutable

class RiverGenerator(val height: Int, val width: Int, val obstaclesFreq: Double, val obstaclesThresh: Double) {
  val obstaclesPerlin: PerlinNoise = PerlinNoise(128)

  //create noise for obstacles on map
  val points: Seq[Vector[Int]] = Vector.tabulate(height, width)((y, x) => if(obstaclesPerlin.noise(y*obstaclesFreq, x*obstaclesFreq) > obstaclesThresh) 1 else 0)

  //method for getting all surrounding successors
  def getSuccessors(position: Vector2D): Seq[Vector2D] =
    for(i <- -1 to 1; j <- -1 to 1 if (i != 0 || j != 0) && position.x + i >= 0 && position.y + j >= 0 && position.x + i < width && position.y + j < height && points(position.y+j)(position.x+i) == 0)
      yield Vector2D(position.x + i, position.y + j)

  def getRiverPoints(start: Vector2D, end: Vector2D): Seq[Vector[Int]] = {
    //obstacles.addPoint(start.y*squareSide, start.x*squareSide, 1)

    def compare(other: (Vector2D, Int)): Int = other._2

    val open = mutable.PriorityQueue[(Vector2D, Int)]()(Ordering.by(compare).reverse)
    //hashmap of visited/closed points
    val closed = mutable.HashMap[Vector2D, Int]()
    //hashmap of all nodes
    val nodes = mutable.HashMap[Vector2D, (Int, Int, Int, Vector2D)]()
    var pathFound = false

    for(i <- points.indices; j <- points(i).indices)
      nodes.update(Vector2D(j, i), (Int.MaxValue, Int.MaxValue, Int.MaxValue, null))

    open.enqueue((start, 0))
    nodes.update(start, (0, 0, start.getRoundDist(end), start))

    def computeSuccessors(q: (Vector2D, Int)): Boolean ={
      getSuccessors(q._1) foreach {
        v: Vector2D =>
          if (!closed.contains(v)) {
            val parent = q._1
            val sucG = nodes(parent)._2 + v.getRoundDist(parent)
            val sucH = v.getRoundDist(end)
            val sucF = sucG + sucH

            nodes.update(v, (sucF, sucG, sucH, parent))

            if (v == end) {
              return true
            }

            var skip = false
            for (node <- open) {
              if (node._1 == v && node._2 <= sucF)
                skip = true
            }

            for (node <- closed) {
              if (node._1 == v && node._2 <= sucF)
                skip = true
            }

            if (!skip)
              open.enqueue((v, sucF))
          }
      }
      false
    }

    while(open.nonEmpty && !pathFound) {
      val q = open.dequeue()
      pathFound = computeSuccessors(q)
      closed.update(q._1, q._2)
    }

    val riverPoints = Array.tabulate(height, width)((_, _) => 0)
    if(pathFound) {
      var curr = end
      while(curr != start) {
        riverPoints(curr.y)(curr.x) = 1
        for(i <- -1 to 1; j <- -1 to 1 if curr.x + i >= 0 && curr.y + j >= 0 && curr.x + i < width && curr.y + j < height)
          riverPoints(curr.y+j)(curr.x+i) = 1
        curr = nodes(curr)._4
      }
      riverPoints(curr.y)(curr.x) = 1
    }
    val riverPointsV = Vector.tabulate(height, width)((y, x) => riverPoints(y)(x))
    riverPointsV
  }
}

object RiverGenerator {
  def apply(height: Int, width: Int, obstaclesFreq: Double, obstaclesThresh: Double) = new RiverGenerator(height, width, obstaclesFreq, obstaclesThresh)
}
