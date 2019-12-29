package org.sorted.chaos.wavefront.reader

import org.joml.Vector2f

object UVCoordinate extends FloatExtractor {
  implicit class ExtractUVCoordinateFrom(val line: String) {
    def getUVCoordinate: Vector2f = {
      val tuple = extract(line)
      validateInput(line, tuple.lineParts, tuple.numbers)

      new Vector2f(
        tuple.numbers(0),
        tuple.numbers(1)
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
