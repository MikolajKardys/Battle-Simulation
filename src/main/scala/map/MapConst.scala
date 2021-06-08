package map

object MapConst {
  val inTerrainIsoValue: Array[Int] = Array(3, 5, 6, 7, 9, 10, 11, 12, 13, 14 ,15)

  val forestFrequency = 0.02
  val terrainFrequency = 0.06
  val sparseForestThresh = 0.3
  val denseForestThresh: Double = sparseForestThresh + 0.1

  val obstaclesFrequency = 0.15
  val obstaclesThresh = 0.2

  //the higher this numbers are the better visibility is
  val fromForestVisibility = 5
  val toForestVisibility = 1
  val throughForestVisibility = 10
}
