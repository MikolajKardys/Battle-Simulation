package agents

class Agent(var posVector: Vector2D, var direction: Vector2D){
  class Position (override val x: Double, override val y: Double) extends Vector2D(x, y){
    def getVector(other: Position): Vector2D = {
      Vector2D(this.x - other.x, this.y - other.y)
    }
    def getDistance(other: Position): Double = {
      (this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y)
    }

  }
  var position: Position = new Position(posVector.x, posVector.y)


  // Wartości które będą zmieniały się w zależności od typu jednostki
  val range: Double = 1.5

  val tokensMax: Int = 5      // Bazowa wartość; im szybsza jednostka tym wyższa ta wartość
  var tokens: Int = 0


  // Blok akcji
  object ActionType extends Enumeration {
    type ActionType = Value
    val Fight, Brace, Flee = Value
  }
  import ActionType._

  def chooseAction(): ActionType =  {  // TODO: Ma zależeć od morale
    Fight
  }
  def doAction(): Unit = {
    if (tokens < tokensMax) {
      tokens += 1
    }
    else {
      val enemy = new Agent(position, direction)  // TODO: Najbliższy widoczny wrogi agent



      chooseAction() match {
        case Fight =>

        case Flee =>
          // TODO: Znajdź najbliższą scianę i uciekaj
        case Brace =>   //Nic nie rób; zachowujesz punkty akcji
      }
    }
  }

  def attack(other: Agent): Unit = {
    // TODO: Znajdź odpowiednią ilość obrażeń
  }

}


object Agent{
  def apply(posVector2D: Vector2D, direction: Vector2D): Agent = new Agent(posVector2D, direction)
}
