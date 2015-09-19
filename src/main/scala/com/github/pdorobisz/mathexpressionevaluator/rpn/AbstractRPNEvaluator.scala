package com.github.pdorobisz.mathexpressionevaluator.rpn

import com.github.pdorobisz.mathexpressionevaluator.errors.EvaluatorError
import com.github.pdorobisz.mathexpressionevaluator.tokens.TokenPosition

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}

/**
 * Implementation of Reverse Polish Notation evaluator.
 *
 * @tparam OUT output type
 */
trait AbstractRPNEvaluator[OUT] {

  /**
   * Evaluates operators on stack.
   *
   * @param stack input stack
   * @param output output stack
   * @param condition evaluation condition (if condition is not met by element on stack evaluation loop breaks)
   * @return no result or error when evaluation failed
   */
  protected def evaluateOperatorsOnStack(stack: mutable.Stack[TokenPosition], output: mutable.Stack[OUT], condition: TokenPosition => Boolean): Validation[EvaluatorError, Unit] = {
    while (stack.headOption.exists(condition)) {
      processOperator(stack.pop(), output) match {
        case f@Failure(_) => return f
        case _ => Unit
      }
    }
    Success(Unit)
  }

  protected def processOperator(tokenPosition: TokenPosition, output: mutable.Stack[OUT]): Validation[EvaluatorError, Unit]
}
