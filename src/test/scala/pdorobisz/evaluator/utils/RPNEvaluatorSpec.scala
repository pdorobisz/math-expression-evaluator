package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.prop.Tables.Table
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.errors.MisplacedOperator
import pdorobisz.evaluator.tokens._

import scalaz.{Failure, Success}

class RPNEvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val correctExpressions = Table(
    ("expression", "expected result"),
    (Seq(TokenPosition(0, Value(0))), 0),
    (Seq(TokenPosition(0, Value(1)), TokenPosition(2, Value(2)), TokenPosition(1, Addition)), 3),
    (Seq(TokenPosition(0, Value(1)), TokenPosition(2, Value(2)), TokenPosition(1, Subtraction)), -1)
  )

  val incorrectExpressions = Table(
    ("expression", "expected result"),
    (Seq(TokenPosition(0, Value(10)), TokenPosition(1, Addition), TokenPosition(3, Value(20)), TokenPosition(2, Addition)), MisplacedOperator(1))
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
