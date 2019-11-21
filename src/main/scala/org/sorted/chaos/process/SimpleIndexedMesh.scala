package org.sorted.chaos.process

import org.sorted.chaos.model.Wavefront

final case class SimpleIndexedMesh(vertices: Array[Float], color: Array[Float], indexes: Array[Int])

object SimpleIndexedMesh {
  def from(wavefront: Wavefront, color: SolidColor): SimpleIndexedMesh = {
    val wavefrontVertices = wavefront.vertices
    val wavefrontFaces    = wavefront.faces

    val vertices   = wavefrontVertices.flatMap(point => point.toArray).toArray
    val colorArray = color.toArray
    val col        = Array.tabulate(vertices.length)(index => colorArray(index % 3))
    val indexes    = wavefrontFaces.flatMap(face => face.toArray).toArray
    SimpleIndexedMesh(vertices, col, indexes)
  }
}
