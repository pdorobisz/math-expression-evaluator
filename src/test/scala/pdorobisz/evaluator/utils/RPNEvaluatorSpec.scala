package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.tokens.{Subtraction, Addition, Value, EvaluatorToken}

import scalaz.Success

class RPNEvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val correctExpressions = Table(
    ("expression", "expected result"),
    (Seq(Value(0)), 0),
    (Seq(Value(1), Value(2), Addition), 3),
    (Seq(Value(1), Value(2), Subtraction), -1)
  )

  property("RPNEvaluator should evaluate Reverse Polish Notation expression") {
    forAll(correctExpressions) { (expression: Seq[EvaluatorToken], expected: Int) =>
      RPNEvaluator.evaluate(expression) should be(Success(expected))
    }
  }
}
