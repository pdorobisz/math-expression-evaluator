package pdorobisz.evaluator.utils

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}
import pdorobisz.evaluator.errors.{DivideByZero, MisplacedOperator}
import pdorobisz.evaluator.operators.{Addition, Division, Subtraction, UnaryMinus}
import spire.math.Rational

import scala.collection.mutable
import scalaz.{Failure, Success}


class OperatorEvaluatorSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  private val position = 4
  private val testData = Table(
    ("operator", "values", "expected result", "expected stack"),

    // binary operator
    (Addition, mutable.Stack[Rational](1, 2), Success(3), mutable.Stack[Rational]()),
    (Subtraction, mutable.Stack[Rational](3 ,2 ,4), Success(-1), mutable.Stack[Rational](4)),
    (Division, mutable.Stack[Rational](0, 2), Failure(DivideByZero(position)), mutable.Stack[Rational]()),

    // binary operator, missing arguments
    (Addition, mutable.Stack[Rational](1), Failure(MisplacedOperator(position)), mutable.Stack[Rational]()),
    (Addition, mutable.Stack[Rational](), Failure(MisplacedOperator(position)), mutable.Stack[Rational]()),

    // unary operator
    (UnaryMinus, mutable.Stack[Rational](2, 3), Success(-2), mutable.Stack[Rational](3)),

    // unary operator, missing arguments
    (UnaryMinus, mutable.Stack[Rational](), Failure(MisplacedOperator(position)), mutable.Stack[Rational]())
  )

  property("OperatoEvaluator should evaluate operator") {
    forAll(testData) { (operator, values, expected, expectedStack) =>
      OperatorEvaluator.evaluateOperator(operator, position, values) should be(expected)
      values should be(expectedStack)
    }
  }
}
