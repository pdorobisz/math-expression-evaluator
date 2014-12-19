package pdorobisz.evaluator.errors

trait EvaluatorError {
  val position: Int
}

case class LeftParenthesisNotMatched(position: Int) extends EvaluatorError

case class RightParenthesisNotMatched(position: Int) extends EvaluatorError

case class InvalidIdentifier(position: Int) extends EvaluatorError
