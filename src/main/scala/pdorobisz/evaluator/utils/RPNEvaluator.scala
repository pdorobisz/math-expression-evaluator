package pdorobisz.evaluator.utils

import scala.collection.mutable

/**
 * Reverse Polish Notation evaluator.
 */
object RPNEvaluator {

  def evaluate(expression: Seq[String]): Int = {
    val stack = mutable.Stack[Int]()
    expression foreach {
      case expr@("+" | "-" | "*" | "/") =>
        val (b, a) = (stack.pop(), stack.pop())
        stack.push(evaluate(expr, a, b))
      case value => stack.push(value.toInt)
    }
    stack.pop()
  }

  private def evaluate(function: String, a: Int, b: Int): Int = function match {
    case "+" => a + b
    case "-" => a - b
    case "*" => a * b
    case "/" => a / b
  }
}
