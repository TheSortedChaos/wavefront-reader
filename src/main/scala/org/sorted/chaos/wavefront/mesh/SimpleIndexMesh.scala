package org.sorted.chaos.wavefront.mesh

import org.joml.Vector3f
import org.sorted.chaos.wavefront.reader.Wavefront

/**
  * This model class represents a [[SimpleIndexMesh]] with vertices, a color and an index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param color the array of the color for the VertexBufferObject
  * @param indexes the array of the indexes (used for OpenGL index drawing)
  */
final case class SimpleIndexMesh(vertices: Array[Float], color: Array[Float], indexes: Array[Int])

object SimpleIndexMesh {
  import org.sorted.chaos.wavefront.reader.JomlExtension.Vector3fExtension

  def from(wavefront: Wavefront, color: Vector3f): SimpleIndexMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTriangles = wavefront.triangles

    val vertices   = wavefrontVertices.flatMap(point => point.toVector)
    val colorVector = color.toVector
    val col        = Vector.tabulate(vertices.length)(index => colorVector(index % 3))
    val indexes = wavefrontTriangles
      .flatMap(triangle => {
        triangle.indices.map(_.vertexIndex - 1) // subtract 1 because of starting index obj vs scala (collection)
      })

    SimpleIndexMesh(vertices.toArray, col.toArray, indexes.toArray)
  }
}
