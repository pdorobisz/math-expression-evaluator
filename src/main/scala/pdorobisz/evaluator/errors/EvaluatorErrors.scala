package pdorobisz.evaluator.errors

sealed trait EvaluatorError {
  val position: Int
}

object EvaluatorError {
  def fromOperatorError(position: Int, operatorError: OperatorError): EvaluatorError = operatorError match {
    case DivideByZeroError => DivideByZero(position)
  }
}

case class LeftParenthesisNotMatched(position: Int) extends EvaluatorError

case class RightParenthesisNotMatched(position: Int) extends EvaluatorError

case class InvalidIdentifier(position: Int) extends EvaluatorError

case class MisplacedOperator(position: Int) extends EvaluatorError

case class DivideByZero(position: Int) extends EvaluatorError
