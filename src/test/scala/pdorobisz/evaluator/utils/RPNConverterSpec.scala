package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.Evaluator
import pdorobisz.evaluator.tokens.{Multiplication, Addition, Value, Token}

import scalaz.Success


class RPNConverterSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val correctExpressions = Table(
    ("expression", "expected result"),
    ("0", Seq(Value(0))),
    ("(0)", Seq(Value(0))),
    ("(0)+(1)", Seq(Value(0), Value(1), Addition)),
    ("3+4", Seq(Value(3), Value(4), Addition)),
    ("3+4+5", Seq(Value(3), Value(4), Addition, Value(5), Addition)),
    ("3+4*5", Seq(Value(3), Value(4), Value(5), Multiplication, Addition)),
    ("(2+3)*5", Seq(Value(2), Value(3), Addition, Value(5), Multiplication)),
    ("2*(3+4)", Seq(Value(2), Value(3), Value(4), Addition, Multiplication)),
    ("(2+3)*(4+5)", Seq(Value(2), Value(3), Addition, Value(4), Value(5), Addition, Multiplication))
  )

  val incorrectExpressions = Table(
    "expression",
    "0g",
    "a",
    "1)(",
    "((1)",
    "(1))",
    "(1+2",
    "1+2)"
  )

  property("RPNConverter should convert infix notation to Reverse Polish Notation") {
    forAll(correctExpressions) { (expression: String, expected: Seq[Token]) =>
      RPNConverter.convert(expression) should be(Success(expected))
    }
  }

  property("RPNConverter should return error when incorrect expression") {
    forAll(incorrectExpressions) { (expression) =>
      RPNConverter.convert(expression).isFailure should be(true)
    }
  }
}
