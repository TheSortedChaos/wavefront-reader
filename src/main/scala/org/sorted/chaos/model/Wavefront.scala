package org.sorted.chaos.model

import org.sorted.chaos.utilities.FileReader

final case class Point(x: Float, y: Float, z: Float)

final case class Face(indexOfPoint1: Int, indexOfPoint2: Int, indexOfPoint3: Int)

final case class Wavefront(vertices: Vector[Point], faces: Vector[Face])

object Wavefront {
  def from(filename: String): Wavefront = {
    val lines = FileReader.read(filename)
    lines.foldLeft(Wavefront.empty) { (accumulator, line) =>
      {
        val parts = splitLine(line)
        parts(0) match {
          case "v" =>
            val vertex = Point(
              parts(1).toFloat,
              parts(2).toFloat,
              parts(3).toFloat
            )
            Wavefront(accumulator.vertices :+ vertex, accumulator.faces)
          case "f" =>
            val face = Face(
              parts(1).toInt - 1, // subtracted 1 because .obj count from 1 (not 0)
              parts(2).toInt - 1,
              parts(3).toInt - 1
            )
            Wavefront(accumulator.vertices, accumulator.faces :+ face)
          case _ =>
            accumulator
        }
      }
    }
  }

  private def splitLine(line: String) = line.split(" ").toVector

  private def empty = Wavefront(Vector.empty[Point], Vector.empty[Face])
}
