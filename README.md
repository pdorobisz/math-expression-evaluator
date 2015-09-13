## Mathematical expressions evaluator
Implementation of mathematical expressions evaluator in Scala.
Supported operations and expressions:
* Addition (+)
* Subtraction (-)
* Multiplication (*)
* Division (/)
* Unary minus
* Parentheses

## Set up
Install to your local repository with `sbt publishLocal` and add dependency to your project:

```
libraryDependencies += "com.github.pdorobisz" %% "math-expression-evaluator" % "1.0"
```

## Usage
### Evaluating expressions
`com.github.pdorobisz.mathevaluator.Evaluator.evaluate` takes string representing expression in infix notation (may contain spaces) and returns
evaluation result as `Validation` from [Scalaz](https://github.com/scalaz/scalaz):
```scala
    import com.github.pdorobisz.mathevaluator.Evaluator
    import com.github.pdorobisz.mathevaluator.errors.EvaluatorError
    import spire.math.Rational
    import scalaz.Validation

    val result: Validation[EvaluatorError, Rational] = Evaluator.evaluate("1+2*3")
```

`EvaluatorError` contains position at which evaluation error has occurred. `Rational` (from [Spire](https://github.com/non/spire))
represents evaluated value.

Some examples:
```scala
    import com.github.pdorobisz.mathevaluator.Evaluator

    println(Evaluator.evaluate("1+2*3"))         // Success(7)
    println(Evaluator.evaluate("4 / (2*3 + 2)")) // Success(1/2)
    println(Evaluator.evaluate("4/(1-1)"))       // Failure(DivideByZero(1))
    println(Evaluator.evaluate("2*(3+4"))        // Failure(ParenthesisNotMatched(2))
```

### Converting expressions to postfix form (**Reverse Polish Notation**)
As a part of evaluation process expression is converted to *Reverse Polish Notation*. It is possible to get expression converted to
this form instead of evaluated value:
```scala
    import com.github.pdorobisz.mathevaluator.errors.EvaluatorError
    import com.github.pdorobisz.mathevaluator.tokens.TokenPosition
    import com.github.pdorobisz.mathevaluator.utils.RPNConverter
    import scalaz.Validation

    val result: Validation[EvaluatorError, Seq[TokenPosition]] = RPNConverter.convert("(3+7)*2")
    println(result) // Success(List(TokenPosition(1,Value(3)), TokenPosition(3,Value(7)), TokenPosition(2,Operator(Addition)), TokenPosition(6,Value(2)), TokenPosition(5,Operator(Multiplication))))
```

### Evaluating expressions in postfix form (**Reverse Polish Notation**)
```scala
    import com.github.pdorobisz.mathevaluator.tokens.TokenFactory._
    import com.github.pdorobisz.mathevaluator.utils.RPNEvaluator

    val rpn = Seq(value(1, 2), value(3, 3), addition(2), value(6, 5), multiplication(5)) // (2+3)*5
    println(RPNEvaluator.evaluate(rpn))
```

### Implicit conversions
To make your life easier you can use implicit conversions to add new methods:
```scala
    import com.github.pdorobisz.mathevaluator.utils.EvaluatorImplicits._

    val rpnForm = "3+5*6".toRPN
    val evaluationResult= "3+5*6".evaluateExpression
```
