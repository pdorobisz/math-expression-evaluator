package pdorobisz.evaluator.tokens

import pdorobisz.evaluator.errors.{DivideByZeroError, OperatorError}
import spire.math.Rational

import scalaz.{Failure, Success, Validation}

sealed trait Token

case object LeftParenthesis extends Token

case class Value(value: Rational) extends Token

sealed abstract class Operator(val precedence: Int, val unary: Boolean, private val expression: IndexedSeq[Rational] => Validation[OperatorError, Rational]) extends Token {
  def apply(args: IndexedSeq[Rational]): Validation[OperatorError, Rational] = expression(args)
}

object Operator {
  def unapply(x: (String, String)): Option[Operator] = (x._1, x._2) match {
    case ("+", _) => Some(Addition)
    case ("-", "" | "(") => Some(UnaryMinus)
    case ("-", _) => Some(Subtraction)
    case ("*", _) => Some(Multiplication)
    case ("/", _) => Some(Division)
    case _ => None
  }
}

case object Addition extends Operator(1, false, args => Success(args(0) + args(1)))

case object Subtraction extends Operator(1, false, args => Success(args(0) - args(1)))

case object UnaryMinus extends Operator(2, true, args => Success(-args(0)))

case object Multiplication extends Operator(3, false, args => Success(args(0) * args(1)))

case object Division extends Operator(3, false, args => if (args(1) != Rational(0)) Success(args(0) / args(1)) else Failure(DivideByZeroError))
