package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.EvaluatorError

import scala.collection.mutable
import scalaz.{Success, Failure, Validation}

/**
 * Reverse Polish Notation converter (shunting-yard algorithm).
 */
object RPNConverter {

  private val pattern = """\G(\d+|[\(\)*/+-])""".r

  def convert(expression: String): Validation[EvaluatorError, Seq[String]] = {
    val stack = mutable.Stack[String]()
    val output = mutable.ArrayBuffer[String]()
    var end = 0

    (pattern findAllIn expression).matchData foreach { m => {
      m.matched match {
        case s@("-" | "+") =>
          while (stack.nonEmpty && stack.top.matches("[-+/*]")) output += stack.pop()
          stack.push(s)
        case s@("*" | "/") =>
          while (stack.nonEmpty && stack.top.matches("[/*]")) output += stack.pop()
          stack.push(s)
        case "(" => stack.push("(")
        case ")" =>
          while (stack.nonEmpty && !stack.top.equals("(")) output += stack.pop()
          if (stack.isEmpty) return Failure(EvaluatorError(""))
          stack.pop()
        case value => output += value
      }
      end = m.end
    }
    }

    if(end != expression.length) return Failure(EvaluatorError(""))
    while (stack.nonEmpty && !stack.top.equals("(")) output += stack.pop()
    if (stack.nonEmpty) return Failure(EvaluatorError(""))
    Success(output)
  }
}
