package org.sorted.chaos.model

import org.sorted.chaos.utilities.FileReader

final case class Point(x: Float, y: Float, z: Float) {
  def toArray: Array[Float] = Array(x, y, z)
}

final case class Face(indexOfPoint1: Int, indexOfPoint2: Int, indexOfPoint3: Int)

final case class Wavefront(vertices: Vector[Point], faces: Vector[Face])

object Wavefront {
  private val Space = " "
  private val VertexToken = "v"
  private val FaceToken = "f"

  private def empty = Wavefront(Vector.empty[Point], Vector.empty[Face])

  def from(filename: String): Wavefront = {
    val lines = FileReader.read(filename)
    lines.foldLeft(Wavefront.empty) { (accumulator, line) =>
      {
        val parts = splitLine(line)
        parts(0) match {
          case VertexToken =>
            createVertex(accumulator, parts)
          case FaceToken =>
            createFace(accumulator, parts)
          case _ =>
            accumulator
        }
      }
    }
  }

  private def splitLine(line: String) = line.split(Space).toVector

  private def createVertex(accumulator: Wavefront, parts: Vector[String]) = {
    val vertex = Point(
      parts(1).toFloat,
      parts(2).toFloat,
      parts(3).toFloat
    )
    Wavefront(accumulator.vertices :+ vertex, accumulator.faces)
  }

  /**
    * We subtract 1 from the index because .obj files are count from 1 but the index
    * in the scala Vector data structure starts with 0
    */
  private def createFace(accumulator: Wavefront, parts: Vector[String]) = {
    val face = Face(
      parts(1).toInt - 1,
      parts(2).toInt - 1,
      parts(3).toInt - 1
    )
    Wavefront(accumulator.vertices, accumulator.faces :+ face)
  }
}
