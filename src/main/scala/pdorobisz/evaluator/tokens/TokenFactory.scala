package pdorobisz.evaluator.tokens

import spire.math.Rational

object TokenFactory {
  def value(pos: Int, value: Int): TokenPosition = TokenPosition(pos, Value(Rational(value)))

  def addition(pos: Int): TokenPosition = TokenPosition(pos, Addition)

  def subtraction(pos: Int): TokenPosition = TokenPosition(pos, Subtraction)

  def multiplication(pos: Int): TokenPosition = TokenPosition(pos, Multiplication)

  def division(pos: Int): TokenPosition = TokenPosition(pos, Division)

  def unaryMinus(pos: Int): TokenPosition = TokenPosition(pos, UnaryMinus)

  def leftParen(pos: Int): TokenPosition = TokenPosition(pos, LeftParenthesis)
}
