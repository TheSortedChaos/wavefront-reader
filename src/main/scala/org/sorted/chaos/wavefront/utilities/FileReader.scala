package org.sorted.chaos.wavefront.utilities

import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.{ Failure, Success, Try }

object FileReader {
  private final val Log = LoggerFactory.getLogger(this.getClass)

  def read(filename: String): Vector[String] =
    Try(Source.fromResource(filename).getLines().toVector) match {
      case Success(lines) => lines
      case Failure(exception) =>
        Log.error(
          s"An error occurred during reading '$filename'.",
          exception
        )
        Vector.empty
    }
}
