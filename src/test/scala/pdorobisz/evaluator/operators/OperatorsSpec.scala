package pdorobisz.evaluator.operators

import org.scalatest._

class OperatorsSpec extends FlatSpec with Matchers {
  "OperatorType" should "have correct string representation" in {
    Addition.toString should be("Addition")
    Subtraction.toString should be("Subtraction")
    Multiplication.toString should be("Multiplication")
    Division.toString should be("Division")
    UnaryMinus.toString should be("UnaryMinus")
  }
}
