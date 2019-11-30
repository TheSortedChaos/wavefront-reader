package org.sorted.chaos.process

import org.sorted.chaos.wavefront.reader.Wavefront

final case class SimpleIndexedMesh(vertices: Array[Float], color: Array[Float], indexes: Array[Int])

object SimpleIndexedMesh {
  def from(wavefront: Wavefront, color: SolidColor): SimpleIndexedMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTriangles = wavefront.triangles

    val vertices   = wavefrontVertices.flatMap(point => point.toArray).toArray
    val colorArray = color.toArray
    val col        = Array.tabulate(vertices.length)(index => colorArray(index % 3))
    val indexes = wavefrontTriangles
      .flatMap(triangle => {
        triangle.asVector.map(_.vertexIndex - 1) // subtract 1 because of starting index obj vs scala (collection)
      })
      .toArray
    SimpleIndexedMesh(vertices, col, indexes)
  }
}
