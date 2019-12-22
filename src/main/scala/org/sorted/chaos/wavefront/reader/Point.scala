package org.sorted.chaos.wavefront.reader

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

object Point extends FloatExtractor {
  implicit class ExtractPointFrom(val line: String) {
    def getPoint: Point = {
      val tuple = extract(line)
      validateInput(line, tuple.lineParts, tuple.numbers)

      Point(
        x = tuple.numbers(0),
        y = tuple.numbers(1),
        z = tuple.numbers(2)
      )
    }

    private def validateInput(line: String, lineParts: Array[String], numbers: Array[Float]): Unit = {
      require(
        lineParts.length == 4,
        s"Reading a 'Vertex' or 'Normal' needs 4 parts [token x y z]. Found ${lineParts.length} part(s) in line '$line'."
      )
      require(
        numbers.length == 3,
        s"Reading a 'Vertex' or 'Normal' needs 3 Float numbers. Found ${numbers.length} Float number(s) in line '$line'."
      )
    }
  }
}
