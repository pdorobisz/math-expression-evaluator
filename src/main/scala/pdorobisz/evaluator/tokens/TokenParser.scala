package pdorobisz.evaluator.tokens

import pdorobisz.evaluator.errors.{EvaluatorError, MisplacedOperator, MisplacedParenthesis, MisplacedValue}
import pdorobisz.evaluator.operators._

import scalaz.{Failure, Success, Validation}

object TokenParser {

  def parseToken(position: Int, s: String, previous: Option[Token]): Validation[EvaluatorError, Token] = (s, previous) match {

    case ("(", None | Some(LeftParenthesis | Operator(_))) => Success(LeftParenthesis)
    case ("(", _) => Failure(MisplacedParenthesis(position))

    case (")", Some(RightParenthesis | Value(_))) => Success(RightParenthesis)
    case (")", _) => Failure(MisplacedParenthesis(position))

    case ("+", Some(RightParenthesis | Value(_))) => Success(Operator(Addition))
    case ("+", _) => Failure(MisplacedOperator(position))

    case ("-", Some(RightParenthesis | Value(_))) => Success(Operator(Subtraction))
    case ("-", None | Some(LeftParenthesis)) => Success(Operator(UnaryMinus))
    case ("-", _) => Failure(MisplacedOperator(position))

    case ("*", Some(RightParenthesis | Value(_))) => Success(Operator(Multiplication))
    case ("*", _) => Failure(MisplacedOperator(position))

    case ("/", Some(RightParenthesis | Value(_))) => Success(Operator(Division))
    case ("/", _) => Failure(MisplacedOperator(position))

    case (v, None | Some(LeftParenthesis | Operator(_))) => Success(Value(v.toInt))
    case (v, _) => Failure(MisplacedValue(position))
  }
}
