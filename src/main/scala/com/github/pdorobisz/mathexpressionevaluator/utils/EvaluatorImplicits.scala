package com.github.pdorobisz.mathexpressionevaluator.utils

import com.github.pdorobisz.mathexpressionevaluator.Evaluator
import com.github.pdorobisz.mathexpressionevaluator.errors.EvaluatorError
import com.github.pdorobisz.mathexpressionevaluator.tokens.TokenPosition
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
