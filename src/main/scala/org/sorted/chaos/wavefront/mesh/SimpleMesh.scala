package org.sorted.chaos.wavefront.mesh

import org.joml.Vector3f
import org.sorted.chaos.wavefront.reader.Wavefront

/**
  * This model class represents a [[SimpleMesh]] with vertices and a color, but without a index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param color    the array of the color for the VertexBufferObject
  */
final case class SimpleMesh(vertices: Array[Float], color: Array[Float])

object SimpleMesh extends Geometry {

  private final case class Accumulator(vertices: Vector[Float], color: Vector[Float])

  private def emptyAccumulator = Accumulator(Vector.empty[Float], Vector.empty[Float])

  def from(wavefront: Wavefront, color: Vector3f): SimpleMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTriangles = wavefront.triangles

    val accumulator = wavefrontTriangles.foldLeft(emptyAccumulator) { (accumulator, triangle) =>
      {
        val vertices = getVerticesOfTriangle(triangle, wavefrontVertices)
        val col      = getColorOfTriangle(color)

        Accumulator(
          vertices = accumulator.vertices ++ vertices,
          color    = accumulator.color ++ col
        )
      }
    }

    SimpleMesh(
      vertices = accumulator.vertices.toArray,
      color    = accumulator.color.toArray
    )
  }
}
