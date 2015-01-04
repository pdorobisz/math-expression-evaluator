package pdorobisz.evaluator.errors

sealed trait OperatorError

case object DivideByZeroError extends OperatorError
