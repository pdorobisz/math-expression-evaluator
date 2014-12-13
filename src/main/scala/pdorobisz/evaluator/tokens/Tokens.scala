package pdorobisz.evaluator.tokens

sealed trait Token

case object LeftParenthesis extends Token

sealed trait EvaluatorToken extends Token

case class Value(value: Int) extends EvaluatorToken

sealed abstract class Operator(val precedence: Int, val expression: (Int, Int) => Int) extends EvaluatorToken {
  def apply(a: Int, b: Int): Int = expression(a, b)
}

object Operator {
  def unapply(s: String): Option[Operator] = s match {
    case "+" => Some(Addition)
    case "-" => Some(Subtraction)
    case "*" => Some(Multiplication)
    case "/" => Some(Division)
    case _ => None
  }
}

case object Addition extends Operator(1, _ + _)

case object Subtraction extends Operator(1, _ - _)

case object Multiplication extends Operator(2, _ * _)

case object Division extends Operator(2, _ / _)