package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.EvaluatorError
import pdorobisz.evaluator.utils.Operators.IsOperator

import scala.collection.mutable
import scalaz.{Success, Validation}

/**
 * Reverse Polish Notation evaluator.
 */
object RPNEvaluator {

  def evaluate(expression: Seq[String]): Validation[EvaluatorError, Int] = {
    val stack = mutable.Stack[Int]()
    expression foreach {
      case s@IsOperator() =>
        val (b, a) = (stack.pop(), stack.pop())
        val operator = Operators.operators(s)
        stack.push(operator(a, b))
      case value => stack.push(value.toInt)
    }
    Success(stack.pop())
  }
}
