package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.errors.MisplacedOperator
import pdorobisz.evaluator.tokens._

import scalaz.{Failure, Success}

class RPNEvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers with TokenFactory {

  val correctExpressions = Table(
    ("expression", "expected result"),
    (Seq(value(0, 0)), 0),
    (Seq(value(0, 1), value(2, 2), addition(1)), 3),
    (Seq(value(0, 1), value(2, 2), subtraction(1)), -1)
  )

  val incorrectExpressions = Table(
    ("expression", "expected result"),
    (Seq(value(0, 10), addition(1), value(3, 20), addition(2)), MisplacedOperator(1))
  )

  property("RPNEvaluator should evaluate Reverse Polish Notation expression") {
    forAll(correctExpressions) { (expression: Seq[TokenPosition], expected: Int) =>
      RPNEvaluator.evaluate(expression) should be(Success(expected))
    }
  }

  property("RPNEvaluator should return error when incorrect expression") {
    forAll(incorrectExpressions) { (expression, expected) =>
      RPNEvaluator.evaluate(expression) should be(Failure(expected))
    }
  }
}
