package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.{ Color, Wavefront }

/**
  * This model class represents a Mesh with vertices and a color, but without a index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param color the array of the color for the VertexBufferObject
  */
final case class SimpleMesh(vertices: Array[Float], color: Array[Float])

object SimpleMesh extends Mesh {

  private def empty = SimpleMesh(Array.emptyFloatArray, Array.emptyFloatArray)

  def from(wavefront: Wavefront, color: Color): SimpleMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTriangles = wavefront.triangles

    wavefrontTriangles.foldLeft(SimpleMesh.empty) { (accumulator, triangle) =>
      {
        val vertices = getVerticesOfTriangle(triangle, wavefrontVertices)
        val col      = getColorOfTriangle(color)

        SimpleMesh(
          vertices = accumulator.vertices ++ vertices,
          color    = accumulator.color ++ col
        )
      }
    }
  }
}
