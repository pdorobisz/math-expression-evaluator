package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.{MisplacedOperator, EvaluatorError}
import pdorobisz.evaluator.tokens.{Operator, TokenPosition, Value}
import spire.math.Rational

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}

/**
 * Reverse Polish Notation evaluator.
 */
object RPNEvaluator {

  /**
   * Evaluates expression in Reverse Polish Notation.
   *
   * @param expression sequence of tokens representing expression in Reverse Polish Notation
   * @return value of expression or error
   */
  def evaluate(expression: Seq[TokenPosition]): Validation[EvaluatorError, Rational] = {
    val stack = mutable.Stack[Rational]()
    expression foreach {
      case TokenPosition(pos, operator: Operator) => getArguments(stack) match {
        case Some((arg1, arg2)) => operator(arg1, arg2) match {
          case Success(result) => stack.push(result)
          case Failure(error) => return Failure(EvaluatorError.fromOperatorError(pos, error))
        }
        case None => return Failure(MisplacedOperator(pos))
      }
      case TokenPosition(pos, Value(value)) => stack.push(value)
    }
    Success(stack.pop())
  }

  private def getArguments(stack: mutable.Stack[Rational]): Option[(Rational, Rational)] = {
    if (stack.nonEmpty) {
      val arg2 = stack.pop()
      if (stack.nonEmpty) {
        val arg1 = stack.pop()
        return Some((arg1, arg2))
      }
    }
    None
  }
}
