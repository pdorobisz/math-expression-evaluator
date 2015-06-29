package pdorobisz.evaluator.test

import org.scalatest.prop.Tables.Table
import pdorobisz.evaluator.errors._
import pdorobisz.evaluator.test.utils.TokenFactory._
import spire.math.Rational

object TestData {

  val correctExpressions = Table(
    ("expression", "RPN form", "evaluation result"),

    // single value
    ("10", Seq(value(0, 10)), 10),
    ("-10", Seq(value(1, 10), unaryMinus(0)), -10),
    ("- 10", Seq(value(2, 10), unaryMinus(0)), -10),
    (" 10", Seq(value(1, 10)), 10),
    ("  10  ", Seq(value(2, 10)), 10),
    ("(10)", Seq(value(1, 10)), 10),
    ("((10))", Seq(value(2, 10)), 10),

    // operators
    ("3+5", Seq(value(0, 3), value(2, 5), addition(1)), 8),
    ("3-5", Seq(value(0, 3), value(2, 5), subtraction(1)), -2),
    ("3*5", Seq(value(0, 3), value(2, 5), multiplication(1)), 15),
    ("8/4", Seq(value(0, 8), value(2, 4), division(1)), 2),
    ("4/6", Seq(value(0, 4), value(2, 6), division(1)), Rational(2, 3)),

    // operators priority
    ("3+5-6", Seq(value(0, 3), value(2, 5), addition(1), value(4, 6), subtraction(3)), 2),
    ("15/5*6", Seq(value(0, 15), value(3, 5), division(2), value(5, 6), multiplication(4)), 18),
    ("3+5*6", Seq(value(0, 3), value(2, 5), value(4, 6), multiplication(3), addition(1)), 33),
    ("4+12/4", Seq(value(0, 4), value(2, 12), value(5, 4), division(4), addition(1)), 7),
    ("-2+3", Seq(value(1, 2), unaryMinus(0), value(3, 3), addition(2)), 1),
    ("-2*3", Seq(value(1, 2), value(3, 3), multiplication(2), unaryMinus(0)), -6),

    // expressions with parenthesis
    ("(5)+(6)", Seq(value(1, 5), value(5, 6), addition(3)), 11),
    ("-(5+6)", Seq(value(2, 5), value(4, 6), addition(3), unaryMinus(0)), -11),
    ("(5)+(-6)", Seq(value(1, 5), value(6, 6), unaryMinus(5), addition(3)), -1),
    ("(2+3)*5", Seq(value(1, 2), value(3, 3), addition(2), value(6, 5), multiplication(5)), 25),
    ("(2-3)*5", Seq(value(1, 2), value(3, 3), subtraction(2), value(6, 5), multiplication(5)), -5),
    ("2*(3+4)", Seq(value(0, 2), value(3, 3), value(5, 4), addition(4), multiplication(1)), 14),
    ("(3+4)*2", Seq(value(1, 3), value(3, 4), addition(2), value(6, 2), multiplication(5)), 14),
    ("(2+3)*(4+5)", Seq(value(1, 2), value(3, 3), addition(2), value(7, 4), value(9, 5), addition(8), multiplication(5)), 45),
    ("(2/3)*(3/4)", Seq(value(1, 2), value(3, 3), division(2), value(7, 3), value(9, 4), division(8), multiplication(5)), Rational(1, 2)),
    ("(2/3)/(3/4)", Seq(value(1, 2), value(3, 3), division(2), value(7, 3), value(9, 4), division(8), division(5)), Rational(8, 9)),
    ("4/(-6)", Seq(value(0, 4), value(4, 6), unaryMinus(3), division(1)), Rational(-2, 3)),

    // expressions with spaces
    ("1 - 2", Seq(value(0, 1), value(4, 2), subtraction(2)), -1),
    (" ( 2 + 3 ) * 5 ", Seq(value(3, 2), value(7, 3), addition(5), value(13, 5), multiplication(11)), 25),
    ("   (  2   + 3 ) *5       ", Seq(value(6, 2), value(12, 3), addition(10), value(17, 5), multiplication(16)), 25),

    // complex expressions
    ("81/9/3", Seq(value(0, 81), value(3, 9), division(2), value(5, 3), division(4)), 3),
    ("81/(9/3)", Seq(value(0, 81), value(4, 9), value(6, 3), division(5), division(2)), 27),
    ("81-9-3", Seq(value(0, 81), value(3, 9), subtraction(2), value(5, 3), subtraction(4)), 69),
    ("81-(9-3)", Seq(value(0, 81), value(4, 9), value(6, 3), subtraction(5), subtraction(2)), 75),
    ("1/2-2/3", Seq(value(0, 1), value(2, 2), division(1), value(4, 2), value(6, 3), division(5), subtraction(3)), Rational(-1, 6)),
    ("1/2+2/3", Seq(value(0, 1), value(2, 2), division(1), value(4, 2), value(6, 3), division(5), addition(3)), Rational(7, 6))
  )

  val notParsableExpressions = Table(
    ("not parsable expression", "expected error"),
    ("0g", InvalidIdentifier(1)),
    ("a", InvalidIdentifier(0)),
    ("(0", ParenthesisNotMatched(0)),
    ("1)(", ParenthesisNotMatched(1)),
    ("1 )(", ParenthesisNotMatched(2)),
    ("((1)", ParenthesisNotMatched(0)),
    ("(1))", ParenthesisNotMatched(3)),
    ("(1 ) )", ParenthesisNotMatched(5)),
    ("(1+2", ParenthesisNotMatched(0)),
    ("1+2)", ParenthesisNotMatched(3)),
    ("(1+2)+(3+", ParenthesisNotMatched(6)),

    // misplaced operators
    ("+10", MisplacedOperator(0)),
    ("*10", MisplacedOperator(0)),
    ("/10", MisplacedOperator(0)),
    ("+1 2", MisplacedOperator(0)),
    ("5++6", MisplacedOperator(2)),
    ("5+-6", MisplacedOperator(2)),
    ("5+*6", MisplacedOperator(2)),
    ("5+/6", MisplacedOperator(2)),
    ("(+6)", MisplacedOperator(1)),
    ("(*6)", MisplacedOperator(1)),
    ("(/6)", MisplacedOperator(1)),
    ("++1", MisplacedOperator(0)),
    ("--1", MisplacedOperator(1)),
    ("- - 1", MisplacedOperator(2)),
    ("+", MisplacedOperator(0)),

    // misplaced values
    ("5 6", MisplacedValue(2)),
    ("5+6 7", MisplacedValue(4)),
    ("(6)7", MisplacedValue(3)),

    // misplaced parenthesis
    ("()", MisplacedParenthesis(1)),
    (")", MisplacedParenthesis(0)),
    ("1(2)", MisplacedParenthesis(1)),
    ("(1)(2)", MisplacedParenthesis(3)),
    ("1+)2", MisplacedParenthesis(2))
  )

  val notEvaluableExpressions = Table(
    ("not evaluable expression", "RPN form", "expected error"),

    // empty expression
    ("", Seq(), EmptyExpression(0)),
    (" ", Seq(), EmptyExpression(0)),

    // misplaced operators
    ("10+", Seq(value(0, 10), addition(2)), MisplacedOperator(2)),
    ("-", Seq(unaryMinus(0)), MisplacedOperator(0)),

    // divide by zero
    ("1/0", Seq(value(0, 1), value(2, 0), division(1)), DivideByZero(1)),
    ("(1/0)", Seq(value(1, 1), value(3, 0), division(2)), DivideByZero(2)),
    ("1/1/0", Seq(value(0, 1), value(2, 1), division(1), value(4, 0), division(3)), DivideByZero(3)),
    ("1/(2-2)", Seq(value(0, 1), value(3, 2), value(5, 2), subtraction(4), division(1)), DivideByZero(1))
  )
}
