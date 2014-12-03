package pdorobisz.evaluator

import pdorobisz.evaluator.utils.{RPNEvaluator, RPNConverter}


/**
 * Mathematical expressions evaluator.
 */
object Evaluator {

  private val pattern = """\G(\d+|[+-])""".r

  def evaluate(expression: String): Option[Int] = {
    RPNConverter.convert(expression) map RPNEvaluator.evaluate
  }
}
