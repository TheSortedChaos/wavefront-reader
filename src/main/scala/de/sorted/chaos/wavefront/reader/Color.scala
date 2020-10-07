package de.sorted.chaos.wavefront.reader

import org.joml.Vector3f

object Color extends FloatExtractor {
  implicit class ExtractColorFrom(val line: String) {
    def getColor: Vector3f = {
      val tuple = extract(line)
      validateInput(line, tuple.lineParts, tuple.numbers)

      new Vector3f(
        tuple.numbers(0),
        tuple.numbers(1),
        tuple.numbers(2)
      )
    }

    private def validateInput(line: String, lineParts: Array[String], numbers: Array[Float]): Unit = {
      require(
        lineParts.length == 4,
        s"Reading a 'Color' needs 4 parts [token red green blue]. Found ${lineParts.length} part(s) in line '$line'."
      )
      require(
        numbers.length == 3,
        s"Reading a 'Color' needs 3 Float numbers. Found ${numbers.length} Float number(s) in line '$line'."
      )
    }
  }
}
