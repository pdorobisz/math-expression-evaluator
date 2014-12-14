package pdorobisz.evaluator

import pdorobisz.evaluator.errors.EvaluatorError
import pdorobisz.evaluator.utils.{RPNConverter, RPNEvaluator}

import scalaz.Validation
import scalaz.Validation.FlatMap._


/**
 * Mathematical expressions evaluator.
 */
object Evaluator {

  /**
   * Evaluates given expression.
   *
   * @param expression string representing expression in infix notation
   * @return value of expression
   */
  def evaluate(expression: String): Validation[EvaluatorError, Int] =
    RPNConverter.convert(expression) flatMap RPNEvaluator.evaluate
}
