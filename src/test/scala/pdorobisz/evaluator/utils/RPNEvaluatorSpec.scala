package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.test.TestData.{correctExpressions, notEvaluableExpressions}

import scalaz.{Failure, Success}

class RPNEvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  property("RPNEvaluator should evaluate Reverse Polish Notation expression") {
    forAll(correctExpressions) { (_, expression, expected) =>
      RPNEvaluator.evaluate(expression) should be(Success(expected))
    }
  }

  property("RPNEvaluator should return error for not evaluable expression") {
    forAll(notEvaluableExpressions) { (_, expression, expected) =>
      RPNEvaluator.evaluate(expression) should be(Failure(expected))
    }
  }
}
