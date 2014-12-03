package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.EvaluatorError

import scala.collection.mutable
import scalaz.{Success, Validation}

/**
 * Reverse Polish Notation evaluator.
 */
object RPNEvaluator {

  def evaluate(expression: Seq[String]): Validation[EvaluatorError, Int] = {
    val stack = mutable.Stack[Int]()
    expression foreach {
      case expr@("+" | "-" | "*" | "/") =>
        val (b, a) = (stack.pop(), stack.pop())
        stack.push(evaluate(expr, a, b))
      case value => stack.push(value.toInt)
    }
    Success(stack.pop())
  }

  private def evaluate(function: String, a: Int, b: Int): Int = function match {
    case "+" => a + b
    case "-" => a - b
    case "*" => a * b
    case "/" => a / b
  }
}
