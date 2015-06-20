package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.{EmptyExpression, EvaluatorError}
import pdorobisz.evaluator.tokens.{Operator, TokenPosition, Value}
import spire.math.Rational

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}

/**
 * Reverse Polish Notation evaluator.
 */
object RPNEvaluator {

  /**
   * Evaluates expression in Reverse Polish Notation.
   *
   * @param expression sequence of tokens representing expression in Reverse Polish Notation
   * @return value of expression or error
   */
  def evaluate(expression: Seq[TokenPosition]): Validation[EvaluatorError, Rational] = {
    val valueStack = mutable.Stack[Rational]()
    expression foreach {
      case TokenPosition(pos, Operator(operator)) => OperatorEvaluator.evaluateOperator(operator, pos, valueStack) match {
        case Success(result) => valueStack.push(result)
        case failure => return failure
      }
      case TokenPosition(pos, Value(value)) => valueStack.push(value)
    }
    if (valueStack.size == 0) Failure(EmptyExpression(0)) else Success(valueStack.pop())
  }
}
