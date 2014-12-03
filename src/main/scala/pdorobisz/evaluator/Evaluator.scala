package pdorobisz.evaluator

import pdorobisz.evaluator.errors.EvaluatorError
import pdorobisz.evaluator.utils.{RPNEvaluator, RPNConverter}

import scalaz.Validation


/**
 * Mathematical expressions evaluator.
 */
object Evaluator {

  private val pattern = """\G(\d+|[+-])""".r

  def evaluate(expression: String): Validation[EvaluatorError, Int] = {
    RPNConverter.convert(expression) flatMap RPNEvaluator.evaluate
  }
}
