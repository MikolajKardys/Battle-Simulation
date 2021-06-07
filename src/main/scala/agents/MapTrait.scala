package agents

trait MapTrait {
  def getNearest(position: Vector2D):  Agent   // Zwraca najbliższego widocznego przeciwnika

  def getNeighbours(position: Vector2D): Seq[Vector2D]
}
