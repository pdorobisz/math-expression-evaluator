package pdorobisz.evaluator

import pdorobisz.evaluator.errors.EvaluatorError
import pdorobisz.evaluator.utils.{RPNConverter, RPNEvaluator}

import scalaz.Validation
import scalaz.Validation.FlatMap._


/**
 * Mathematical expressions evaluator.
 */
object Evaluator {

  def evaluate(expression: String): Validation[EvaluatorError, Int] =
    RPNConverter.convert(expression) flatMap RPNEvaluator.evaluate
}
