package de.sorted.chaos.wavefront.reader

object ExtractSingleFloat extends FloatExtractor {
  implicit class ExtractFloatFrom(val line: String) {
    def getSpecularExponent: Float = {
      val tuple = extract(line)
      validateSpecularExponent(line, tuple.lineParts, tuple.numbers)

      tuple.numbers(0)
    }

    def getAlphaChannel: Float = {
      val tuple = extract(line)
      validateAlphaChannel(line, tuple.lineParts, tuple.numbers)

      tuple.numbers(0)
    }

    private def validateSpecularExponent(line: String, lineParts: Array[String], numbers: Array[Float]): Unit = {
      require(
        lineParts.length == 2,
        s"Reading a 'Specular Exponent' needs 2 parts [token value]. Found ${lineParts.length} part(s) in line '$line'."
      )
      require(
        numbers.length == 1,
        s"Reading a 'Specular Exponent' needs 1 Float number. Found ${numbers.length} Float number(s) in line '$line'."
      )
    }

    private def validateAlphaChannel(line: String, lineParts: Array[String], numbers: Array[Float]): Unit = {
      require(
        lineParts.length == 2,
        s"Reading a 'Alpha Channel' needs 2 parts [token value]. Found ${lineParts.length} part(s) in line '$line'."
      )
      require(
        numbers.length == 1,
        s"Reading a 'Alpha Channel' needs 1 Float number. Found ${numbers.length} Float number(s) in line '$line'."
      )
    }
  }
}
