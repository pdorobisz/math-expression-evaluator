package com.github.pdorobisz.mathevaluator.operators

import com.github.pdorobisz.mathevaluator.errors.{EvaluatorError, MisplacedOperator}
import spire.math.Rational

import scala.collection.mutable
import scalaz.{Failure, Validation}

object OperatorEvaluator {

  /**
   * Evaluates given operator. Arguments used by operator are removed from stack.
   *
   * @param operator operator
   * @param pos operator's position in expression
   * @param valueStack stack containing values
   * @return evaluation result or error
   */
  def evaluateOperator(operator: OperatorType, pos: Int, valueStack: mutable.Stack[Rational]): Validation[EvaluatorError, Rational] = getArguments(valueStack, operator) match {
    case Some(args) => operator(args).leftMap(EvaluatorError.fromOperatorError(pos, _))
    case None => Failure(MisplacedOperator(pos))
  }

  private def getArguments(valueStack: mutable.Stack[Rational], operator: OperatorType): Option[IndexedSeq[Rational]] = {
    if (valueStack.nonEmpty) {
      val arg = valueStack.pop()
      if (operator.unary) return Some(IndexedSeq(arg))
      if (valueStack.nonEmpty) return Some(IndexedSeq(valueStack.pop(), arg))
    }
    None
  }
}
