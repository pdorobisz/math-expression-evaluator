package pdorobisz.evaluator.tokens

import pdorobisz.evaluator.operators._
import spire.math.Rational

object TokenFactory {

  def value(pos: Int, value: Int): TokenPosition = TokenPosition(pos, Value(Rational(value)))

  def addition(pos: Int): TokenPosition = TokenPosition(pos, Operator(Addition))

  def subtraction(pos: Int): TokenPosition = TokenPosition(pos, Operator(Subtraction))

  def multiplication(pos: Int): TokenPosition = TokenPosition(pos, Operator(Multiplication))

  def division(pos: Int): TokenPosition = TokenPosition(pos, Operator(Division))

  def unaryMinus(pos: Int): TokenPosition = TokenPosition(pos, Operator(UnaryMinus))

  def leftParen(pos: Int): TokenPosition = TokenPosition(pos, LeftParenthesis)
}
