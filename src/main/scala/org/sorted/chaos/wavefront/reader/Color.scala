package org.sorted.chaos.wavefront.reader

/**
  * This model class represents the color of the Mesh (when not having a texture)
  *
  * @param red the amount of red a value between 0 and 1
  * @param green the amount of green a value between 0 and 1
  * @param blue the amount of blue a value between 0 and 1
  */
final case class Color(red: Float, green: Float, blue: Float) {
  def toArray: Array[Float] = Array(red, green, blue)
}

object Color extends FloatExtractor {
  implicit class ExtractColorFrom(val line: String) {
    def getColor: Color = {
      val tuple = extract(line)
      validateInput(line, tuple.lineParts, tuple.numbers)

      Color(
        red   = tuple.numbers(0),
        green = tuple.numbers(1),
        blue  = tuple.numbers(2)
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
