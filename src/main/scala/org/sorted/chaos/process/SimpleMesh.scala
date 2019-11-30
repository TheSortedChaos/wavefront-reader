package org.sorted.chaos.process

import org.sorted.chaos.wavefront.reader.Wavefront

/**
  * This class represents the color of the Mesh, when not having a texture
  *
  * @param red the amount of red a value between 0 and 1
  * @param green the amount of green a value between 0 and 1
  * @param blue the amount of blue a value between 0 and 1
  */
final case class SolidColor(red: Float, green: Float, blue: Float) {
  def toArray: Array[Float] = Array(red, green, blue)
}

final case class SimpleMesh(vertices: Array[Float], color: Array[Float])

object SimpleMesh {
  private def empty = SimpleMesh(Array.emptyFloatArray, Array.emptyFloatArray)

  def from(wavefront: Wavefront, color: SolidColor): SimpleMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTriangles = wavefront.triangles

    wavefrontTriangles.foldLeft(SimpleMesh.empty) { (accumulator, triangleDef) =>
      {
        val index1 = triangleDef.point1
        val index2 = triangleDef.point2
        val index3 = triangleDef.point3

        // we have to subtract one because .obj index starts from 1, Scala Collection index starts from 0
        val point1 = wavefrontVertices(index1.vertexIndex - 1)
        val point2 = wavefrontVertices(index2.vertexIndex - 1)
        val point3 = wavefrontVertices(index3.vertexIndex - 1)

        SimpleMesh(
          vertices = accumulator.vertices ++ point1.toArray ++ point2.toArray ++ point3.toArray,
          color    = accumulator.color ++ color.toArray ++ color.toArray ++ color.toArray
        )
      }
    }
  }
}
