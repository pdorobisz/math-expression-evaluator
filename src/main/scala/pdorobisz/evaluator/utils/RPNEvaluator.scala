package pdorobisz.evaluator.utils

import pdorobisz.evaluator.errors.{EmptyExpression, EvaluatorError}
import pdorobisz.evaluator.operators.OperatorEvaluator
import pdorobisz.evaluator.rpn.AbstractRPNEvaluator
import pdorobisz.evaluator.tokens.{Operator, TokenPosition, Value}
import spire.math.Rational

import scala.collection.mutable
import scalaz.{Failure, Success, Validation}

/**
 * Reverse Polish Notation evaluator.
 */
object RPNEvaluator extends AbstractRPNEvaluator[Rational] {

  /**
   * Evaluates expression in Reverse Polish Notation.
   *
   * @param expression sequence of tokens representing expression in Reverse Polish Notation
   * @return value of expression or error
   */
  def evaluate(expression: Seq[TokenPosition]): Validation[EvaluatorError, Rational] = {
    val valueStack = mutable.Stack[Rational]()
    val stack: mutable.Stack[TokenPosition] = mutable.Stack(expression: _*)
    evaluateOperatorsOnStack(stack, valueStack, _ => true) match {
      case Success(_) if valueStack.isEmpty => Failure(EmptyExpression(0))
      case Success(_) => Success(valueStack.pop())
      case f@Failure(_) => f
    }
  }

  override protected def processOperator(tokenPosition: TokenPosition, output: mutable.Stack[Rational]): Validation[EvaluatorError, Unit] =
    tokenPosition match {
      case TokenPosition(pos, Operator(operator)) => OperatorEvaluator.evaluateOperator(operator, pos, output) match {
        case Success(result) =>
          output.push(result)
          Success(Unit)
        case f@Failure(_) => f
      }
      case TokenPosition(pos, Value(value)) =>
        output.push(value)
        Success(Unit)
    }
}
