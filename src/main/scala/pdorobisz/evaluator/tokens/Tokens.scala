package pdorobisz.evaluator.tokens

import pdorobisz.evaluator.errors.{DivideByZeroError, OperatorError}
import spire.math.Rational

import scalaz.{Failure, Success, Validation}

sealed trait Token

case object LeftParenthesis extends Token

case object RightParenthesis extends Token

case class Value(value: Rational) extends Token

case class Operator(operator: OperatorType) extends Token

abstract class OperatorType(val precedence: Int, val unary: Boolean, private val expression: IndexedSeq[Rational] => Validation[OperatorError, Rational]) {
  def apply(args: IndexedSeq[Rational]): Validation[OperatorError, Rational] = expression(args)
}

object Addition extends OperatorType(1, false, args => Success(args(0) + args(1)))

object Subtraction extends OperatorType(1, false, args => Success(args(0) - args(1)))

object UnaryMinus extends OperatorType(2, true, args => Success(-args(0)))

object Multiplication extends OperatorType(3, false, args => Success(args(0) * args(1)))

object Division extends OperatorType(3, false, args => if (args(1) != Rational(0)) Success(args(0) / args(1)) else Failure(DivideByZeroError))
