package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.Wavefront

/**
  * This model class represents a Mesh with vertices, texture and normals, but without a index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject
  * @param normals the array of normals for the VertexBufferObject
  */
final case class WavefrontMesh(vertices: Array[Float], textures: Array[Float], normals: Array[Float])

object WavefrontMesh extends Mesh {

  private def empty = WavefrontMesh(
    vertices = Array.emptyFloatArray,
    textures = Array.emptyFloatArray,
    normals  = Array.emptyFloatArray
  )

  def from(wavefront: Wavefront): WavefrontMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTextures  = wavefront.textures
    val wavefrontNormals   = wavefront.normals
    val wavefrontTriangles = wavefront.triangles

    wavefrontTriangles.foldLeft(WavefrontMesh.empty) { (accumulator, triangle) =>
      {
        val vertices = getVerticesOfTriangle(triangle, wavefrontVertices)
        val textures = getTexturesOfTriangle(triangle, wavefrontTextures)
        val normals  = getNormalsOfTriangle(triangle, wavefrontNormals)

        WavefrontMesh(
          vertices = accumulator.vertices ++ vertices,
          textures = accumulator.textures ++ textures,
          normals  = accumulator.normals ++ normals
        )
      }
    }
  }
}
