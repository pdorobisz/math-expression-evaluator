package pdorobisz.evaluator.tokens

import pdorobisz.evaluator.errors.{DivideByZeroError, OperatorError}

import scalaz.{Failure, Success, Validation}

sealed trait Token

case object LeftParenthesis extends Token

case class Value(value: Int) extends Token

sealed abstract class Operator(val precedence: Int, val expression: (Int, Int) => Validation[OperatorError, Int]) extends Token {
  def apply(a: Int, b: Int): Validation[OperatorError, Int] = expression(a, b)
}

object Operator {
  def unapply(s: String): Option[Operator] = s match {
    case "+" => Some(Addition)
    case "-" => Some(Subtraction)
    case "*" => Some(Multiplication)
    case "/" => Some(Division)
    case _ => None
  }
}

case object Addition extends Operator(1, (x, y) => Success(x + y))

case object Subtraction extends Operator(1, (x, y) => Success(x - y))

case object Multiplication extends Operator(2, (x, y) => Success(x * y))

case object Division extends Operator(2, (x, y) => if (y != 0) Success(x / y) else Failure(DivideByZeroError))
