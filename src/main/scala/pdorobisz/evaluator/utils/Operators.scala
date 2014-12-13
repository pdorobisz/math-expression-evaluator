package pdorobisz.evaluator.utils

object Operators {
  val operators = Map(
    "+" -> Addition,
    "-" -> Subtraction,
    "*" -> Multiplication,
    "/" -> Division
  )

  object IsOperator {
    def apply(s: String): Boolean = operators.contains(s)

    def unapply(s: String): Boolean = apply(s)
  }

}

sealed abstract class Operator(val precedence: Int, val expression: (Int, Int) => Int) {
  def apply(a: Int, b: Int): Int = expression(a, b)
}

case object Addition extends Operator(1, _ + _)

case object Subtraction extends Operator(1, _ - _)

case object Multiplication extends Operator(2, _ * _)

case object Division extends Operator(2, _ / _)