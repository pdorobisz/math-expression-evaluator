package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.{EmptyExpression, MisplacedOperator, EvaluatorError}
import pdorobisz.evaluator.tokens.{OperatorType, Operator, TokenPosition, Value}
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
    val valueStack = mutable.Stack[Rational]()
    expression foreach {
      case TokenPosition(pos, Operator(operator)) => getArguments(valueStack, operator) match {
        case Some(args) => operator(args) match {
          case Success(result) => valueStack.push(result)
          case Failure(error) => return Failure(EvaluatorError.fromOperatorError(pos, error))
        }
        case None => return Failure(MisplacedOperator(pos))
      }
      case TokenPosition(pos, Value(value)) => valueStack.push(value)
    }
    if (valueStack.size == 0)
      Failure(EmptyExpression(0))
    else
      Success(valueStack.pop())
  }

  private def getArguments(valueStack: mutable.Stack[Rational], operator: OperatorType): Option[IndexedSeq[Rational]] = {
    if (valueStack.nonEmpty) {
      val arg = valueStack.pop()
      if (operator.unary) return Some(IndexedSeq(arg))
      if (valueStack.nonEmpty) return Some(IndexedSeq(valueStack.pop(), arg))
    }
    None
  }
}
