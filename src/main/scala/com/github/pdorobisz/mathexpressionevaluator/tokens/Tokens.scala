package com.github.pdorobisz.mathexpressionevaluator.tokens

import com.github.pdorobisz.mathexpressionevaluator.operators.OperatorType
import spire.math.Rational

sealed trait Token

case object LeftParenthesis extends Token

case object RightParenthesis extends Token

case class Value(value: Rational) extends Token

case class Operator(operator: OperatorType) extends Token
