package pdorobisz.evaluator.rpn

import pdorobisz.evaluator.errors.{EvaluatorError, InvalidIdentifier, ParenthesisNotMatched}
import pdorobisz.evaluator.tokens._

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}

/**
 * Implementation of Reverse Polish Notation converter (Shunting-Yard algorithm).
 *
 * @tparam OUT output type
 */
trait AbstractRPNConverter[OUT] {

  private val pattern = """\G(\d+|[\(\)*/+-]|\s+)""".r

  /**
   * Parses given expression.
   *
   * @param expression string representing expression in infix notation
   * @return sequence of tokens representing expression in Reverse Polish Notation or error
   */
  def parseExpression(expression: String): Validation[EvaluatorError, Seq[OUT]] = {
    val expressionWithoutTrailingSpaces = expression.replaceAll( """(?m)\s+$""", "")
    val stack = mutable.Stack[TokenPosition]()
    val output = mutable.Stack[OUT]()
    var endPosition = 0
    var previousToken: Option[Token] = None

    (pattern findAllIn expressionWithoutTrailingSpaces).matchData filterNot (_.matched.trim.isEmpty) foreach { m => {
      val position: Int = m.start
      val parsedToken: Validation[EvaluatorError, Token] = TokenParser.parseToken(position, m.matched, previousToken)

      parsedToken match {
        case Success(v@Value(_)) => output.push(convertValue(position, v))
        case Success(LeftParenthesis) => stack push TokenPosition(position, LeftParenthesis)
        case Success(RightParenthesis) => evaluateOperatorsOnStack(stack, output, _.token != LeftParenthesis) match {
          case Success(_) if stack.nonEmpty => stack.pop()
          case Success(_) => return Failure(ParenthesisNotMatched(position))
          case f@Failure(_) => return f
        }
        case Success(op@Operator(operator)) => evaluateOperatorsOnStack(stack, output, {
          case TokenPosition(_, Operator(operatorOnStack)) => operator.precedence <= operatorOnStack.precedence
          case _ => false
        }) match {
          case Success(_) => stack push TokenPosition(position, op)
          case f@Failure(_) => return f
        }
        case f@Failure(_) => return f
      }

      previousToken = parsedToken.map(Some(_)).getOrElse(None)
      endPosition = m.end
    }
    }

    if (endPosition != expressionWithoutTrailingSpaces.length) return Failure(InvalidIdentifier(endPosition))
    evaluateOperatorsOnStack(stack, output, _.token != LeftParenthesis) match {
      case Success(_) if stack.isEmpty => Success(output)
      case Success(_) => Failure(ParenthesisNotMatched(stack.head.position))
      case f@Failure(_) => f
    }
  }

  protected def evaluateOperatorsOnStack(stack: mutable.Stack[TokenPosition], output: mutable.Stack[OUT], condition: TokenPosition => Boolean): Validation[EvaluatorError, Unit]

  protected def convertValue(position: Int, v: Value): OUT
}
