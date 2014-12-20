package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.errors.{InvalidIdentifier, LeftParenthesisNotMatched, RightParenthesisNotMatched}
import pdorobisz.evaluator.tokens._

import scalaz.{Failure, Success}


class RPNConverterSpec extends PropSpec with TableDrivenPropertyChecks with Matchers with TokenFactory {

  val correctExpressionsWithoutPositions = Table(
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

  val correctExpressions = Table(
    ("expression", "expected result"),
    ("10", Seq(value(0, 10))),
    ("(10)", Seq(value(1, 10))),
    ("(2+3)*5", Seq(value(1, 2), value(3, 3), addition(2), value(6, 5), multiplication(5))),
    ("2*(3+4)", Seq(value(0, 2), value(3, 3), value(5, 4), addition(4), multiplication(1))),
    ("(2+3)*(4+5)", Seq(value(1, 2), value(3, 3), addition(2), value(7, 4), value(9, 5), addition(8), multiplication(5))),
    ("10+", Seq(value(0, 10), addition(2))),
    ("5++6", Seq(value(0, 5), addition(1), value(3, 6), addition(2)))
  )

  val incorrectExpressions = Table(
    ("expression", "expected result"),
    ("0g", InvalidIdentifier(1)),
    ("a", InvalidIdentifier(0)),
    ("1)(", RightParenthesisNotMatched(1)),
    ("((1)", LeftParenthesisNotMatched(0)),
    ("(1))", RightParenthesisNotMatched(3)),
    ("(1+2", LeftParenthesisNotMatched(0)),
    ("1+2)", RightParenthesisNotMatched(3))
  )

  property("RPNConverter should convert infix notation to Reverse Polish Notation (without positions)") {
    forAll(correctExpressionsWithoutPositions) { (expression: String, expected: Seq[Token]) =>
      RPNConverter.convert(expression).getOrElse(Seq()) map (_.token) should be(expected)
    }
  }

  property("RPNConverter should convert infix notation to Reverse Polish Notation") {
    forAll(correctExpressions) { (expression: String, expected: Seq[TokenPosition]) =>
      RPNConverter.convert(expression) should be(Success(expected))
    }
  }

  property("RPNConverter should return error when incorrect expression") {
    forAll(incorrectExpressions) { (expression, expected) =>
      RPNConverter.convert(expression) should be(Failure(expected))
    }
  }
}
