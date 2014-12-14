package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.EvaluatorError
import pdorobisz.evaluator.tokens._

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}

/**
 * Reverse Polish Notation converter (shunting-yard algorithm).
 */
object RPNConverter {

  private val pattern = """\G(\d+|[\(\)*/+-])""".r

  /**
   * Converts expression in infix notation to Reverse Polish Notation expression.
   *
   * @param expression expression in infix notation
   * @return sequence of tokens representing expression in Reverse Polish Notation or error
   */
  def convert(expression: String): Validation[EvaluatorError, Seq[EvaluatorToken]] = {
    val stack = mutable.Stack[Token]()
    val output = mutable.ArrayBuffer[EvaluatorToken]()
    var end = 0

    (pattern findAllIn expression).matchData foreach { m => {
      m.matched match {
        case Operator(s) =>
          while (stack.nonEmpty && stack.top.isInstanceOf[Operator] && s.precedence <= stack.top.asInstanceOf[Operator].precedence) output += stack.pop().asInstanceOf[EvaluatorToken]
          stack.push(s)
        case "(" => stack.push(LeftParenthesis)
        case ")" =>
          while (stack.nonEmpty && stack.top != LeftParenthesis) output += stack.pop().asInstanceOf[EvaluatorToken]
          if (stack.isEmpty) return Failure(EvaluatorError(""))
          stack.pop()
        case value => output += Value(value.toInt)
      }
      end = m.end
    }
    }

    if (end != expression.length) return Failure(EvaluatorError(""))
    while (stack.nonEmpty && stack.top != LeftParenthesis) output += stack.pop().asInstanceOf[EvaluatorToken]
    if (stack.nonEmpty) return Failure(EvaluatorError(""))
    Success(output)
  }
}
