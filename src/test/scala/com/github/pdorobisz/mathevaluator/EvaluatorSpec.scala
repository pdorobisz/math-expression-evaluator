package com.github.pdorobisz.mathevaluator

import com.github.pdorobisz.mathevaluator.test.TestData
import com.github.pdorobisz.mathevaluator.test.TestData.{correctExpressions, notEvaluableExpressions, notParsableExpressions}
import org.scalatest._
import org.scalatest.prop._

import scalaz.{Failure, Success}

class EvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  property("Evaluator should evaluate correct expression") {
    forAll(correctExpressions) { (expression, _, expected) =>
      Evaluator.evaluate(expression) should be(Success(expected))
    }
  }

  property("Evaluator should return error for not parsable expression") {
    forAll(notParsableExpressions) { (expression, expected) =>
      Evaluator.evaluate(expression) should be(Failure(expected))
    }
  }

  property("Evaluator should return error for not evaluable expression") {
    forAll(notEvaluableExpressions) { (expression, _, expected) =>
      Evaluator.evaluate(expression) should be(Failure(expected))
    }
  }
}
