package org.sorted.chaos.wavefront.reader

import org.sorted.chaos.wavefront.reader.Wavefront.Space

/**
  * This model class represents the Texture definition of the .obj file, starting with `vt ...`
  * Example:
  * * `vt 0.609836 0.758661`
  *
  * @param u coordinate
  * @param v coordinate
  */
final case class UVCoordinate(u: Float, v: Float)

object UVCoordinate {
  def from(line: String): Either[String, UVCoordinate] = {
    val lineParts = line.split(Space)
    val numbers   = lineParts.tail.flatMap(_.toFloatOption)

    if (lineParts.length != 3) {
      Left(
        "  * There are 3 arguments [token number number] needed to parse a Texture coordinate, " +
        s"but ${lineParts.length} argument(s) was/were found (source was: '$line')."
      )
    } else if (numbers.length != 2) {
      Left(
        "  * There are 2 numbers needed to parse a Texture coordinate, but something could not transformed " +
        s"to a Float (source was: '$line)'."
      )
    } else {
      Right(UVCoordinate(numbers(0), numbers(1)))
    }
  }

}
