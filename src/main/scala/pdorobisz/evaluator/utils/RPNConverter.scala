package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.EvaluatorError
import pdorobisz.evaluator.tokens._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
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
  def convert(expression: String): Validation[EvaluatorError, Seq[TokenPosition]] = {
    val stack = mutable.Stack[TokenPosition]()
    val output = mutable.ArrayBuffer[TokenPosition]()
    var endPosition = 0

    (pattern findAllIn expression).matchData foreach { m => {
      m.matched match {
        case Operator(operator) =>
          while (hasLowerPrecedence(operator, stack)) output += popToken(stack)
          stack.push(TokenPosition(m.start, operator))
        case "(" => stack.push(TokenPosition(m.start, LeftParenthesis))
        case ")" =>
          addOperatorsToOutput(stack, output)
          if (stack.isEmpty) return Failure(EvaluatorError(""))
          stack.pop()
        case value => output += TokenPosition(m.start, Value(value.toInt))
      }
      endPosition = m.end
    }
    }

    if (endPosition != expression.length) return Failure(EvaluatorError(""))
    addOperatorsToOutput(stack, output)
    if (stack.nonEmpty) return Failure(EvaluatorError(""))
    Success(output)
  }

  private def popToken(stack: mutable.Stack[TokenPosition]): TokenPosition = stack.pop()

  private def hasLowerPrecedence(operator: Operator, stack: mutable.Stack[TokenPosition]): Boolean =
    stack.headOption.exists(_.token.isInstanceOf[Operator]) &&
      operator.precedence <= stack.top.token.asInstanceOf[Operator].precedence

  private def addOperatorsToOutput(stack: mutable.Stack[TokenPosition], output: ArrayBuffer[TokenPosition]) {
    while (stack.headOption.exists(_.token != LeftParenthesis)) output += popToken(stack)
  }

}
