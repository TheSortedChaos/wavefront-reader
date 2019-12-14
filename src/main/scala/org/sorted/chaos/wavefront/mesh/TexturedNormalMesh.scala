package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.Wavefront

/**
  * This model class represents a Mesh with vertices, texture and normals but without a index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject
  * @param normals the array of normals for the VertexBufferObject
  */
final case class TexturedNormalMesh(vertices: Array[Float], textures: Array[Float], normals: Array[Float])

object TexturedNormalMesh extends Mesh {

  private def empty =
    TexturedNormalMesh(
      Array.emptyFloatArray,
      Array.emptyFloatArray,
      Array.emptyFloatArray
    )

  def from(wavefront: Wavefront): TexturedNormalMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTextures  = wavefront.textures
    val wavefrontNormals   = wavefront.normals
    val wavefrontTriangles = wavefront.triangles

    wavefrontTriangles.foldLeft(TexturedNormalMesh.empty) { (accumulator, triangle) =>
      {
        val vertices = getVerticesOfTriangle(triangle, wavefrontVertices)
        val textures = getTexturesOfTriangle(triangle, wavefrontTextures)
        val normals  = getNormalsOfTriangle(triangle, wavefrontNormals)

        TexturedNormalMesh(
          vertices = accumulator.vertices ++ vertices,
          textures = accumulator.textures ++ textures,
          normals  = accumulator.normals ++ normals
        )
      }
    }
  }
}
