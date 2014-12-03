package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}

import scalaz.Success

class RPNEvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val correctExpressions = Table(
    ("expression", "expected result"),
    (Seq("0"), 0),
    (Seq("1", "2", "+"), 3),
    (Seq("1", "2", "-"), -1)
  )

  property("RPNEvaluator should evaluate Reverse Polish Notation expression") {
    forAll(correctExpressions) { (expression: Seq[String], expected: Int) =>
      RPNEvaluator.evaluate(expression) should be(Success(expected))
    }
  }
}
