package pdorobisz.evaluator.tokens

import pdorobisz.evaluator.errors.{DivideByZeroError, OperatorError}
import spire.math.Rational

import scalaz.{Failure, Success, Validation}

sealed trait Token

case object LeftParenthesis extends Token

case class Value(value: Rational) extends Token

sealed abstract class Operator(val precedence: Int, val expression: (Rational, Rational) => Validation[OperatorError, Rational]) extends Token {
  def apply(a: Rational, b: Rational): Validation[OperatorError, Rational] = expression(a, b)
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

case object Division extends Operator(2, (x, y) => if (y != Rational(0)) Success(x / y) else Failure(DivideByZeroError))
