package agents

class Engine (val rows: Int, val cols: Int){

  var agentList: List[Agent] = List[Agent]()

  def getMoves(position: Vector2D): Seq[Vector2D] = {
    val thisX = position.x
    val thisY = position.y

    val neighFields: Seq[Vector2D] =
        for (i <- (-1).until(1) if thisX + i >= 0 && thisX + i < cols;
             j <- (-1).until(1) if thisY + j >= 0 && thisY + j < rows)
          yield new Vector2D(thisX + i, thisY + j)

    val fieldsTaken = for (agent <- agentList) yield agent.posVector

    val freeNeighbours = for (position <- neighFields if !fieldsTaken.contains(position)) yield position

    freeNeighbours
  }
}
