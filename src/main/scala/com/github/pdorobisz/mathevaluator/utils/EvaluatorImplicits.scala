package com.github.pdorobisz.mathevaluator.utils

import com.github.pdorobisz.mathevaluator.Evaluator
import com.github.pdorobisz.mathevaluator.errors.EvaluatorError
import com.github.pdorobisz.mathevaluator.tokens.TokenPosition
import spire.math.Rational

import scalaz.Validation

/**
 * Implicit conversions.
 */
object EvaluatorImplicits {

  implicit class EvaluableString(s: String) {
    def evaluateExpression: Validation[EvaluatorError, Rational] = Evaluator.evaluate(s)

    def toRPN: Validation[EvaluatorError, Seq[TokenPosition]] = RPNConverter.convert(s)
  }

}
