package pdorobisz.evaluator.utils

import scala.collection.mutable

/**
 * Reverse Polish Notation converter (shunting-yard algorithm).
 */
object RPN {

  private val pattern = """\G(\d+|[\(\)*/+-])""".r

  def convert(expression: String): Option[Seq[String]] = {
    val stack = mutable.Stack[String]()
    val output = mutable.ArrayBuffer[String]()
    var end = 0

    (pattern findAllIn expression).matchData foreach { m => {
      m.matched match {
        case s@("-" | "+") =>
          while (stack.nonEmpty && stack.top.matches("[-+/*]")) output += stack.pop()
          stack.push(s)
        case s@("*" | "/") =>
          while (stack.nonEmpty && stack.top.matches("[/*]")) output += stack.pop()
          stack.push(s)
        case "(" => stack.push("(")
        case ")" =>
          while (stack.nonEmpty && !stack.top.equals("(")) output += stack.pop()
          if (stack.isEmpty) return None
          stack.pop()
        case value => output += value
      }
      end = m.end
    }
    }

    if(end != expression.length) return None
    while (stack.nonEmpty && !stack.top.equals("(")) output += stack.pop()
    if (stack.nonEmpty) return None
    Some(output)
  }
}
