package org.sorted.chaos

import org.slf4j.LoggerFactory

// $COVERAGE-OFF$
object HelloWorld {
  final val Log = LoggerFactory.getLogger(HelloWorld.getClass)

  def main(args: Array[String]): Unit =
    Log.info(s"2 + 3 = ${SimpleFunction.add(2, 3)}")
}
// $COVERAGE-ON$
