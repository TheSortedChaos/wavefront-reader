package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.Wavefront

/**
  * This model class represents a [[Mesh]] with vertices, texture and normals, but without a index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject, will be empty, if no data for this type was found
  * @param normals the array of normals for the VertexBufferObject, will be empty, if no data for this type was found
  */
final case class Mesh(vertices: Array[Float], textures: Array[Float], normals: Array[Float])

object Mesh extends Geometry {

  private def empty = Mesh(
    vertices = Array.emptyFloatArray,
    textures = Array.emptyFloatArray,
    normals  = Array.emptyFloatArray
  )

  def from(wavefront: Wavefront): Mesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTextures  = wavefront.textures
    val wavefrontNormals   = wavefront.normals
    val wavefrontTriangles = wavefront.triangles

    wavefrontTriangles.foldLeft(Mesh.empty) { (accumulator, triangle) =>
      {
        val vertices = getVerticesOfTriangle(triangle, wavefrontVertices)
        val textures = getTexturesOfTriangle(triangle, wavefrontTextures)
        val normals  = getNormalsOfTriangle(triangle, wavefrontNormals)

        Mesh(
          vertices = accumulator.vertices ++ vertices,
          textures = accumulator.textures ++ textures,
          normals  = accumulator.normals ++ normals
        )
      }
    }
  }
}
