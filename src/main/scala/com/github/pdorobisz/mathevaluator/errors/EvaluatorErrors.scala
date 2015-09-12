package com.github.pdorobisz.mathevaluator.errors

sealed trait EvaluatorError {
  val position: Int
}

object EvaluatorError {
  def fromOperatorError(position: Int, operatorError: OperatorError): EvaluatorError = operatorError match {
    case DivideByZeroError => DivideByZero(position)
  }
}

case class ParenthesisNotMatched(position: Int) extends EvaluatorError

case class EmptyExpression(position: Int) extends EvaluatorError

case class InvalidIdentifier(position: Int) extends EvaluatorError

case class MisplacedOperator(position: Int) extends EvaluatorError

case class MisplacedValue(position: Int) extends EvaluatorError

case class MisplacedParenthesis(position: Int) extends EvaluatorError

case class DivideByZero(position: Int) extends EvaluatorError
