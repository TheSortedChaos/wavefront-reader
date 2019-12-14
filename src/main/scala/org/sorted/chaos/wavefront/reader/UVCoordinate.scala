package org.sorted.chaos.wavefront.reader

/**
  * This model class represents the Texture definition of the .obj file, starting with `vt ...`
  * Example:
  * * `vt 0.609836 0.758661`
  *
  * @param u coordinate
  * @param v coordinate
  */
final case class UVCoordinate(u: Float, v: Float) {
  def toArray: Array[Float] = Array(u, v)
}

object UVCoordinate extends FloatExtractor {
  implicit class ExtractUVCoordinateFrom(val line: String) {
    def getUVCoordinate: UVCoordinate = {
      val tuple = extract(line)
      validateInput(line, tuple.lineParts, tuple.numbers)

      UVCoordinate(
        u = tuple.numbers(0),
        v = tuple.numbers(1)
      )
    }

    private def validateInput(line: String, lineParts: Array[String], numbers: Array[Float]): Unit = {
      require(
        lineParts.length == 3,
        s"Reading a 'Texture' needs 3 parts [token u v]. Found ${lineParts.length} part(s) in line '$line'."
      )
      require(
        numbers.length == 2,
        s"Reading a 'Texture' needs 2 Float numbers. Found ${numbers.length} Float number(s) in line '$line'."
      )
    }
  }
}
