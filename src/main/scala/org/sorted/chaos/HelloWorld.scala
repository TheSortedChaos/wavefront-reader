package org.sorted.chaos

import org.slf4j.LoggerFactory

object HelloWorld {
  final val Log = LoggerFactory.getLogger(HelloWorld.getClass)

  def main(args: Array[String]): Unit = {
    Log.trace("Hello World!")
    Log.debug("Hello World!")
    Log.info("Hello World!")
    Log.warn("Hello World!")
    Log.error("Hello World!")
  }
}
