package de.sorted.chaos.wavefront.utilities

import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.{ Failure, Success, Try }

object FileReader {
  private final val Log = LoggerFactory.getLogger(this.getClass)

  def read(filename: String): Vector[String] = {
    val maybeStream = Try(this.getClass.getResourceAsStream(filename))
    maybeStream match {
      case Failure(exception) =>
        Log.error(
          s"Can't create an InputStream from file: '$filename'. Exception was: ",
          exception
        )
        Vector.empty[String]
      case Success(inStream) =>
        Try(Source.fromInputStream(inStream).getLines().toVector) match {
          case Success(lines) => lines
          case Failure(exception) =>
            Log.error(
              s"An error occurred during reading '$filename'. Exception was: ",
              exception
            )
            Vector.empty[String]
        }
    }
  }
}
