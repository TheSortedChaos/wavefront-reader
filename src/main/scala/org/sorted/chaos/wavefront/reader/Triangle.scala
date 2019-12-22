package org.sorted.chaos.wavefront.reader

import org.sorted.chaos.wavefront.reader.Wavefront.Space

/**
  *  This model class represents the Triangle definition of the .obj file, starting with `f ...`.
  *  There are several abilities.
  *  Examples:
  *  * `f 7 6 8` - only vertices
  *  * `f 1/1 2/2 3/3 4/4` - vertex & texture
  *  * `f 1//3 4//5 2//8` - vertex & normals
  *  * `f 28/28/28 79/29/29 78/27/27` - vertex, texture & normal
  *
  * @param point1 The indices for the first point of the triangle
  * @param point2 The indices for the second point of the triangle
  * @param point3 The indices for the third point of the triangle
  */
final case class Triangle(point1: Indices, point2: Indices, point3: Indices) {

  def indices: Vector[Indices] = Vector(point1, point2, point3)
}

object Triangle {
  import Indices._

  implicit class ExtractTriangleFrom(val line: String) {
    def getTriangle: Triangle = {
      val lineParts = line.split(Space)
      val indices   = lineParts.tail.flatMap(part => part.getIndices)
      validateInput(line, lineParts, indices)

      Triangle(
        indices(0),
        indices(1),
        indices(2)
      )
    }

    private def validateInput(line: String, lineParts: Array[String], indices: Array[Indices]): Unit = {
      require(
        lineParts.length == 4,
        s"Reading a 'Triangle' needs 4 parts [token indices indices indices]. Found ${lineParts.length} part(s) in line '$line'."
      )
      require(
        indices.length == 3,
        s"Creating a 'Triangle' needs 3 Indices. Found ${indices.length} indices in line '$line'."
      )
    }
  }
}
