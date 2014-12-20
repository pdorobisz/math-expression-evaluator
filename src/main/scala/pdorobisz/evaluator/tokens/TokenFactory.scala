package pdorobisz.evaluator.tokens

trait TokenFactory {
  def value(pos: Int, value: Int): TokenPosition = TokenPosition(pos, Value(value))

  def addition(pos: Int): TokenPosition = TokenPosition(pos, Addition)

  def subtraction(pos: Int): TokenPosition = TokenPosition(pos, Subtraction)

  def multiplication(pos: Int): TokenPosition = TokenPosition(pos, Multiplication)

  def division(pos: Int): TokenPosition = TokenPosition(pos, Division)
}
