package pdorobisz.evaluator.operators

import pdorobisz.evaluator.errors.{DivideByZeroError, OperatorError}
import spire.math.Rational

import scalaz.{Failure, Success, Validation}

abstract class OperatorType(val precedence: Int, val unary: Boolean, private val expression: IndexedSeq[Rational] => Validation[OperatorError, Rational]) {
  def apply(args: IndexedSeq[Rational]): Validation[OperatorError, Rational] = expression(args)

  override def toString =  this.getClass.getSimpleName.dropRight(1)
}

object Addition extends OperatorType(1, false, args => Success(args(0) + args(1)))

object Subtraction extends OperatorType(1, false, args => Success(args(0) - args(1)))

object UnaryMinus extends OperatorType(2, true, args => Success(-args(0)))

object Multiplication extends OperatorType(3, false, args => Success(args(0) * args(1)))

object Division extends OperatorType(3, false, args => if (args(1) != Rational(0)) Success(args(0) / args(1)) else Failure(DivideByZeroError))
