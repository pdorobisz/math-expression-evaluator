package pdorobisz.evaluator

import org.scalatest._
import org.scalatest.prop._
import pdorobisz.evaluator.errors.LeftParenthesisNotMatched

import scalaz.{Failure, Success}

class EvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val correctExpressions = Table(
    ("expression", "expected result"),
    ("0", 0),
    ("(0)", 0),
    ("(0)+(1)", 1),
    ("3+4", 7),
    ("3+4+5", 12),
    ("3+4*5", 23),
    ("(2+3)*5", 25),
    ("2*(3+4)", 14),
    ("(2+3)*(4+5)", 45)
  )

  val incorrectExpressions = Table(
    ("expression", "expected result"),
    ("(0", LeftParenthesisNotMatched(0))
  )

  property("evaluator should evaluate correct expression") {
    forAll(correctExpressions) { (expression, expected) =>
      Evaluator.evaluate(expression) should be(Success(expected))
    }
  }

  property("evaluator should return None for incorrect expression") {
    forAll(incorrectExpressions) { (expression, expected) =>
      Evaluator.evaluate(expression) should be(Failure(expected))
    }
  }
}
