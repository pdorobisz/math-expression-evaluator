package com.github.pdorobisz.mathexpressionevaluator.errors

sealed trait OperatorError

case object DivideByZeroError extends OperatorError
