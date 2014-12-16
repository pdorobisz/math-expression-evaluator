package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.tokens._

import scalaz.Success

class RPNEvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val correctExpressions = Table(
    ("expression", "expected result"),
    (Seq(TokenPosition(0, Value(0))), 0),
    (Seq(TokenPosition(0, Value(1)), TokenPosition(2, Value(2)), TokenPosition(1, Addition)), 3),
    (Seq(TokenPosition(0, Value(1)), TokenPosition(2, Value(2)), TokenPosition(1, Subtraction)), -1)
  )

  property("RPNEvaluator should evaluate Reverse Polish Notation expression") {
    forAll(correctExpressions) { (expression: Seq[TokenPosition], expected: Int) =>
      RPNEvaluator.evaluate(expression) should be(Success(expected))
    }
  }
}
