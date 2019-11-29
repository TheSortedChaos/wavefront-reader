package org.sorted.chaos.wavefront.reader

import org.sorted.chaos.wavefront.reader.Wavefront.Space

/**
  * This model class represents the Vertex/Normal definition of the .obj file, starting with `v ...` or `vn ...`
  * Example
  * * `v 0.230970 -1.000000 0.095671` (Vertex)
  * or
  * * `vn 0.6894 -0.6657 0.2855` (Normal)
  *
  * @param x coordinate
  * @param y coordinate
  * @param z coordinate
  */
final case class Point(x: Float, y: Float, z: Float) {
  def toArray: Array[Float] = Array(x, y, z)
}

object Point {
  def from(line: String): Either[String, Point] = {
    val lineParts = line.split(Space)
    val numbers   = lineParts.tail.flatMap(_.toFloatOption)

    if (lineParts.length != 4) {
      Left(
        "  * There are 4 arguments [token number number number] needed to parse a Vertex/Normal coordinate, " +
        s"but ${lineParts.length} argument(s) was/were found (source was: '$line')."
      )
    } else if (numbers.length != 3) {
      Left(
        "  * There are 3 numbers needed to parse a Vertex/Normal coordinate, but something could not transformed " +
        s"to a Float (source was: '$line')."
      )
    } else {
      Right(Point(numbers(0), numbers(1), numbers(2)))
    }
  }
}
