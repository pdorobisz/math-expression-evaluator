package com.github.pdorobisz.mathevaluator.utils

import com.github.pdorobisz.mathevaluator.test.TestData
import com.github.pdorobisz.mathevaluator.test.TestData.{correctExpressions, notEvaluableExpressions, notParsableExpressions}
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}

import scalaz.{Failure, Success}

class RPNConverterSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  property("RPNConverter should convert infix notation to Reverse Polish Notation") {
    forAll(correctExpressions) { (expression, expected, _) =>
      RPNConverter.convert(expression) should be(Success(expected))
    }
  }

  property("RPNConverter should convert not evaluable expression to Reverse Polish Notation") {
    forAll(notEvaluableExpressions) { (expression, expected, _) =>
      RPNConverter.convert(expression) should be(Success(expected))
    }
  }

  property("RPNConverter should return error for not parsable expression") {
    forAll(notParsableExpressions) { (expression, expected) =>
      RPNConverter.convert(expression) should be(Failure(expected))
    }
  }
}
