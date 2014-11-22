package pdorobisz.evaluator

import org.scalatest._
import org.scalatest.prop._

class EvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val correctExpressions = Table(
    ("expression", "expected result"),
    ("0", 0)
  )

  val incorrectExpressions = Table(
    "expression",
    "0+"
  )

  property("evaluator should evaluate correct expression") {
    forAll(correctExpressions) { (expression, expected) =>
      Evaluator.evaluate(expression) should be(Some(expected))
    }
  }

  property("evaluator should return None for incorrect expression") {
    forAll(incorrectExpressions) { (expression) =>
      Evaluator.evaluate(expression) should be(None)
    }
  }
}
