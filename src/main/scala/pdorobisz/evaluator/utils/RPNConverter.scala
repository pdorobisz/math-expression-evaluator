package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.{EvaluatorError, InvalidIdentifier, LeftParenthesisNotMatched, RightParenthesisNotMatched}
import pdorobisz.evaluator.tokens._

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}

/**
 * Reverse Polish Notation converter (shunting-yard algorithm).
 */
object RPNConverter {

  private val pattern = """\G(\d+|[\(\)*/+-]|\s+)""".r

  /**
   * Converts expression in infix notation to Reverse Polish Notation expression.
   *
   * @param expression expression in infix notation
   * @return sequence of tokens representing expression in Reverse Polish Notation or error
   */
  def convert(expression: String): Validation[EvaluatorError, Seq[TokenPosition]] = {
    val expressionWithoutTrailingSpaces = expression.replaceAll( """(?m)\s+$""", "")
    val stack = mutable.Stack[TokenPosition]()
    val output = mutable.ArrayBuffer[TokenPosition]()
    var endPosition = 0
    var previousToken: Option[Token] = None

    (pattern findAllIn expressionWithoutTrailingSpaces).matchData filterNot (_.matched.trim.isEmpty) foreach { m => {
      val position: Int = m.start
      val token: Token = getToken(m.matched, previousToken)

      token match {
        case LeftParenthesis =>
          stack push TokenPosition(position, LeftParenthesis)
        case RightParenthesis =>
          while (leftParenthesisNotOnTop(stack)) output += stack.pop()
          if (stack.isEmpty) return Failure(RightParenthesisNotMatched(position))
          stack.pop()
        case v@Value(_) =>
          output += TokenPosition(position, v)
        case op@Operator(operator) =>
          while (operatorOnStackHasHigherPrecedence(operator, stack)) output += stack.pop()
          stack push TokenPosition(position, op)
      }
      previousToken = Some(token)
      endPosition = m.end
    }
    }

    if (endPosition != expressionWithoutTrailingSpaces.length) return Failure(InvalidIdentifier(endPosition))
    while (leftParenthesisNotOnTop(stack)) output += stack.pop()
    if (stack.nonEmpty) return Failure(LeftParenthesisNotMatched(stack.head.position))
    Success(output)
  }

  private def getToken(s: String, previous: Option[Token]): Token = s match {
    case "(" => LeftParenthesis
    case ")" => RightParenthesis
    case "+" => Operator(Addition)
    case "-" => if (previous.isEmpty || previous.get == LeftParenthesis) Operator(UnaryMinus) else Operator(Subtraction)
    case "*" => Operator(Multiplication)
    case "/" => Operator(Division)
    case v => Value(v.toInt)
  }

  private def operatorOnStackHasHigherPrecedence(operator: OperatorType, stack: mutable.Stack[TokenPosition]): Boolean =
    stack.headOption match {
      case Some(TokenPosition(_, Operator(operatorOnStack))) => operator.precedence <= operatorOnStack.precedence
      case _ => false
    }

  def leftParenthesisNotOnTop(stack: mutable.Stack[TokenPosition]): Boolean = {
    stack.headOption.exists(_.token != LeftParenthesis)
  }
}
