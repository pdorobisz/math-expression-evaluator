package com.github.pdorobisz.mathevaluator.errors

sealed trait OperatorError

case object DivideByZeroError extends OperatorError
