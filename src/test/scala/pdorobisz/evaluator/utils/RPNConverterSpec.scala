package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.Evaluator


class RPNConverterSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val correctExpressions = Table(
    ("expression", "expected result"),
    ("0", Seq("0")),
    ("(0)", Seq("0")),
    ("(0)+(1)", Seq("0", "1", "+")),
    ("3+4", Seq("3", "4", "+")),
    ("3+4+5", Seq("3", "4", "+", "5", "+")),
    ("3+4*5", Seq("3", "4", "5", "*", "+")),
    ("(2+3)*5", Seq("2", "3", "+", "5", "*")),
    ("2*(3+4)", Seq("2", "3", "4", "+", "*")),
    ("(2+3)*(4+5)", Seq("2", "3", "+", "4", "5", "+", "*"))
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
    forAll(correctExpressions) { (expression:String, expected:Seq[String]) =>
      RPNConverter.convert(expression) should be(Some(expected))
    }
  }

  property("RPNConverter should return error when incorrect expression") {
    forAll(incorrectExpressions) { (expression) =>
      RPNConverter.convert(expression) should be(None)
    }
  }
}
