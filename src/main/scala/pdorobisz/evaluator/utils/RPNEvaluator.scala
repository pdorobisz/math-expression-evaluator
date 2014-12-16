package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.EvaluatorError
import pdorobisz.evaluator.tokens.{Operator, TokenPosition, Value}

import scala.collection.mutable
import scalaz.{Success, Validation}

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
  def evaluate(expression: Seq[TokenPosition]): Validation[EvaluatorError, Int] = {
    val stack = mutable.Stack[Int]()
    expression foreach {
      case TokenPosition(pos, operator: Operator) =>
        val (b, a) = (stack.pop(), stack.pop())
        stack.push(operator(a, b))
      case TokenPosition(pos, Value(value)) => stack.push(value)
    }
    Success(stack.pop())
  }
}
