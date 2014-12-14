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
  def convert(expression: String): Validation[EvaluatorError, Seq[EvaluatorToken]] = {
    val stack = mutable.Stack[Token]()
    val output = mutable.ArrayBuffer[EvaluatorToken]()
    var endPosition = 0

    (pattern findAllIn expression).matchData foreach { m => {
      m.matched match {
        case Operator(operator) =>
          while (hasLowerPrecedence(operator, stack)) output += popToken(stack)
          stack.push(operator)
        case "(" => stack.push(LeftParenthesis)
        case ")" =>
          addOperatorsToOutput(stack, output)
          if (stack.isEmpty) return Failure(EvaluatorError(""))
          stack.pop()
        case value => output += Value(value.toInt)
      }
      endPosition = m.end
    }
    }

    if (endPosition != expression.length) return Failure(EvaluatorError(""))
    addOperatorsToOutput(stack, output)
    if (stack.nonEmpty) return Failure(EvaluatorError(""))
    Success(output)
  }

  private def popToken(stack: mutable.Stack[Token]): EvaluatorToken = stack.pop().asInstanceOf[EvaluatorToken]

  private def hasLowerPrecedence(operator: Operator, stack: mutable.Stack[Token]): Boolean =
    stack.headOption.exists(_.isInstanceOf[Operator]) && operator.precedence <= stack.top.asInstanceOf[Operator].precedence

  private def addOperatorsToOutput(stack: mutable.Stack[Token], output: ArrayBuffer[EvaluatorToken]) {
    while (stack.headOption.exists(_ != LeftParenthesis)) output += popToken(stack)
  }

}
