package pdorobisz.evaluator


/**
 * Mathematical expressions evaluator.
 */
object Evaluator {

  private val pattern = """\G(\d+|[+-])""".r

  def evaluate(expression: String): Option[Int] = {
    //    val stack = Stack
    pattern findAllIn expression foreach {
      case s => println(s)
    }
    Some(0)
  }
}
