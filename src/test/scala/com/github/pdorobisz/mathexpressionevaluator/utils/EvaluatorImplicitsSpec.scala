package com.github.pdorobisz.mathexpressionevaluator.utils

import com.github.pdorobisz.mathexpressionevaluator.tokens.TokenFactory
import com.github.pdorobisz.mathexpressionevaluator.tokens.TokenFactory._
import org.scalatest.{FlatSpec, Matchers}

import scalaz.Success

class EvaluatorImplicitsSpec extends FlatSpec with Matchers {

  "EvaluatorImplicits" should "add evaluate method to String" in {
    import com.github.pdorobisz.mathexpressionevaluator.utils.EvaluatorImplicits._
    val result = "3*(2+3) + 4/2".evaluateExpression
    result should be(Success(17))
  }

  it should "add toRPN method to String" in {
    import com.github.pdorobisz.mathexpressionevaluator.utils.EvaluatorImplicits._
    val result = "3+5*6".toRPN
    result should be(Success(Seq(TokenFactory.value(0, 3), TokenFactory.value(2, 5), TokenFactory.value(4, 6), multiplication(3), addition(1))))
  }
}
