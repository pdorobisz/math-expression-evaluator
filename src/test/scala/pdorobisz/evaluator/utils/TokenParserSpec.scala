package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.errors.{MisplacedOperator, MisplacedValue, MisplacedParenthesis}
import pdorobisz.evaluator.tokens._

import scalaz.{Failure, Success}

class TokenParserSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  private val position = 4

  private val data = Table(
    ("string", "previous token", "expected result"),

    ("(", None, Success(LeftParenthesis)),
    ("(", Some(LeftParenthesis), Success(LeftParenthesis)),
    ("(", Some(Operator(Addition)), Success(LeftParenthesis)),
    ("(", Some(RightParenthesis), Failure(MisplacedParenthesis(position))),
    ("(", Some(Value(0)), Failure(MisplacedParenthesis(position))),

    (")", Some(Value(0)), Success(RightParenthesis)),
    (")", None, Failure(MisplacedParenthesis(position))),
    (")", Some(Operator(Addition)), Failure(MisplacedParenthesis(position))),
    (")", Some(LeftParenthesis), Failure(MisplacedParenthesis(position))),

    ("123", None, Success(Value(123))),
    ("123", Some(Operator(Addition)), Success(Value(123))),
    ("123", Some(LeftParenthesis), Success(Value(123))),
    ("123", Some(Value(0)), Failure(MisplacedValue(position))),
    ("123", Some(RightParenthesis), Failure(MisplacedValue(position))),

    ("+", Some(RightParenthesis), Success(Operator(Addition))),
    ("+", Some(Value(0)), Success(Operator(Addition))),
    ("+", None, Failure(MisplacedOperator(position))),
    ("+", Some(LeftParenthesis), Failure(MisplacedOperator(position))),
    ("+", Some(Operator(Addition)), Failure(MisplacedOperator(position))),

    ("-", Some(RightParenthesis), Success(Operator(Subtraction))),
    ("-", Some(Value(0)), Success(Operator(Subtraction))),
    ("-", None, Success(Operator(UnaryMinus))),
    ("-", Some(LeftParenthesis), Success(Operator(UnaryMinus))),
    ("-", Some(Operator(Addition)), Failure(MisplacedOperator(position))),

    ("*", Some(RightParenthesis), Success(Operator(Multiplication))),
    ("*", Some(Value(0)), Success(Operator(Multiplication))),
    ("*", None, Failure(MisplacedOperator(position))),
    ("*", Some(LeftParenthesis), Failure(MisplacedOperator(position))),
    ("*", Some(Operator(Addition)), Failure(MisplacedOperator(position))),

    ("/", Some(RightParenthesis), Success(Operator(Division))),
    ("/", Some(Value(0)), Success(Operator(Division))),
    ("/", None, Failure(MisplacedOperator(position))),
    ("/", Some(LeftParenthesis), Failure(MisplacedOperator(position))),
    ("/", Some(Operator(Addition)), Failure(MisplacedOperator(position)))
  )

  property("TokenParser should parse tokens") {
    forAll(data) { (s, previous, expected) =>
      TokenParser.parseToken(position, s, previous) should be(expected)
    }
  }
}
