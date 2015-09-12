package com.github.pdorobisz.mathevaluator

import com.github.pdorobisz.mathevaluator.errors.{EmptyExpression, EvaluatorError}
import com.github.pdorobisz.mathevaluator.operators.OperatorEvaluator
import com.github.pdorobisz.mathevaluator.rpn.{AbstractRPNConverter, AbstractRPNEvaluator}
import com.github.pdorobisz.mathevaluator.tokens.{Operator, TokenPosition, Value}
import spire.math.Rational

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}


/**
 * Mathematical expressions evaluator.
 */
object Evaluator extends AbstractRPNConverter[Rational] with AbstractRPNEvaluator[Rational] {

  /**
   * Evaluates given expression.
   *
   * @param expression string representing expression in infix notation
   * @return value of expression
   */
  def evaluate(expression: String): Validation[EvaluatorError, Rational] = parseExpression(expression) match {
    case Success(output) if output.isEmpty => Failure(EmptyExpression(0))
    case Success(output) => Success(output(0))
    case f@Failure(_) => f
  }

  override def convertValue(position: Int, v: Value): Rational = v.value

  override def processOperator(tokenPosition: TokenPosition, output: mutable.Stack[Rational]): Validation[EvaluatorError, Unit] =
    tokenPosition match {
      case TokenPosition(position, Operator(operator)) => OperatorEvaluator.evaluateOperator(operator, position, output) match {
        case Success(v) =>
          output.push(v)
          Success(Unit)
        case f@Failure(_) => f
      }
      case _ => Success(Unit)
    }
}
