package pdorobisz.evaluator

import pdorobisz.evaluator.errors.{EmptyExpression, EvaluatorError, InvalidIdentifier, ParenthesisNotMatched}
import pdorobisz.evaluator.tokens._
import pdorobisz.evaluator.utils.{OperatorEvaluator, TokenParser}
import spire.math.Rational

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}


/**
 * Mathematical expressions evaluator.
 */
object Evaluator {

  private val pattern = """\G(\d+|[\(\)*/+-]|\s+)""".r

  /**
   * Evaluates given expression.
   *
   * @param expression string representing expression in infix notation
   * @return value of expression
   */
  def evaluate(expression: String): Validation[EvaluatorError, Rational] = {
    val expressionWithoutTrailingSpaces = expression.replaceAll( """(?m)\s+$""", "")
    val stack = mutable.Stack[TokenPosition]()
    val valuesStack = mutable.Stack[Rational]()
    var endPosition = 0
    var previousToken: Option[Token] = None

    (pattern findAllIn expressionWithoutTrailingSpaces).matchData filterNot (_.matched.trim.isEmpty) foreach { m => {
      val position: Int = m.start
      val parsedToken: Validation[EvaluatorError, Token] = TokenParser.parseToken(position, m.matched, previousToken)

      parsedToken match {
        case Success(Value(v)) => valuesStack.push(v)
        case Success(LeftParenthesis) => stack push TokenPosition(position, LeftParenthesis)
        case Success(RightParenthesis) => evaluateOperatorsOnStack(stack, valuesStack, _.token != LeftParenthesis) match {
          case Success(_) => if (stack.nonEmpty) stack.pop() else return Failure(ParenthesisNotMatched(position))
          case f@Failure(_) => return f
        }
        case Success(op@Operator(operator)) =>
          evaluateOperatorsOnStack(stack, valuesStack, {
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
    evaluateOperatorsOnStack(stack, valuesStack, _.token != LeftParenthesis) match {
      case Success(_) =>
        if (stack.nonEmpty) return Failure(ParenthesisNotMatched(stack.head.position))
        if (valuesStack.isEmpty) Failure(EmptyExpression(0)) else Success(valuesStack.pop())
      case f@Failure(_) => f
    }
  }

  private def evaluateOperatorsOnStack(stack: mutable.Stack[TokenPosition], valuesStack: mutable.Stack[Rational], condition: TokenPosition => Boolean): Validation[EvaluatorError, Unit] = {
    while (stack.headOption.exists(condition)) {
      stack.pop() match {
        case TokenPosition(position, Operator(operator)) => OperatorEvaluator.evaluateOperator(operator, position, valuesStack) match {
          case Success(v) => valuesStack.push(v)
          case f@Failure(_) => return f
        }
        case _ => Unit
      }
    }
    Success(Unit)
  }
}
