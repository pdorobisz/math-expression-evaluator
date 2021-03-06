package com.github.pdorobisz.mathexpressionevaluator.utils

import com.github.pdorobisz.mathexpressionevaluator.errors.EvaluatorError
import com.github.pdorobisz.mathexpressionevaluator.rpn.{AbstractRPNConverter, AbstractRPNEvaluator}
import com.github.pdorobisz.mathexpressionevaluator.tokens.{TokenPosition, Value}

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}

/**
 * Reverse Polish Notation converter.
 */
object RPNConverter extends AbstractRPNConverter[TokenPosition] with AbstractRPNEvaluator[TokenPosition] {

  /**
   * Converts expression in infix notation to Reverse Polish Notation expression.
   *
   * @param expression expression in infix notation
   * @return sequence of tokens representing expression in Reverse Polish Notation or error
   */
  def convert(expression: String): Validation[EvaluatorError, Seq[TokenPosition]] = parseExpression(expression) match {
    case Success(output) => Success(output.toList.reverse)
    case f@Failure(_) => f
  }

  override protected def convertValue(position: Int, v: Value): TokenPosition = TokenPosition(position, v)

  override protected def processOperator(tokenPosition: TokenPosition, output: mutable.Stack[TokenPosition]): Validation[EvaluatorError, Unit] = {
    output.push(tokenPosition)
    Success(Unit)
  }
}
