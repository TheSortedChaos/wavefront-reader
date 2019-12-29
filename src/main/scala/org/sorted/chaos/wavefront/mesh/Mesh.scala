package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.Wavefront

/**
  * This model class represents a [[Mesh]] with vertices, texture and normals, but without a index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject, will be empty, if no data for this type was found
  * @param normals the array of normals for the VertexBufferObject, will be empty, if no data for this type was found
  * @param tangents the array of tangents for the VertexBufferObject (for NormalMapping),
  *                 will be empty, if no data for this type was found
  * @param biTangents the array of biTangents for the VertexBufferObject (for NormalMapping),
  *                   will be empty, if no data for this type was found
  */
final case class Mesh(
    vertices: Array[Float],
    textures: Array[Float],
    normals: Array[Float],
    tangents: Array[Float],
    biTangents: Array[Float]
)

object Mesh extends Geometry {

  def from(wavefront: Wavefront): Mesh = {
    val wavefrontVertices   = wavefront.vertices
    val wavefrontTextures   = wavefront.textures
    val wavefrontNormals    = wavefront.normals
    val wavefrontTangents   = wavefront.tangents
    val wavefrontBiTangents = wavefront.biTangents
    val wavefrontTriangles  = wavefront.triangles

    val accumulator = wavefrontTriangles.foldLeft(emptyAccumulator) { (accumulator, triangle) =>
      {
        val vertices   = getVerticesOfTriangle(triangle, wavefrontVertices)
        val textures   = getTexturesOfTriangle(triangle, wavefrontTextures)
        val normals    = getNormalsOfTriangle(triangle, wavefrontNormals)
        val tangents   = getTangentsOfTriangle(triangle, wavefrontTangents)
        val biTangents = getBiTangentsOfTriangle(triangle, wavefrontBiTangents)

        Accumulator(
          vertices   = accumulator.vertices ++ vertices,
          textures   = accumulator.textures ++ textures,
          normals    = accumulator.normals ++ normals,
          tangents   = accumulator.tangents ++ tangents,
          biTangents = accumulator.biTangents ++ biTangents
        )
      }
    }

    Mesh(
      vertices   = accumulator.vertices.toArray,
      textures   = accumulator.textures.toArray,
      normals    = accumulator.normals.toArray,
      tangents   = accumulator.tangents.toArray,
      biTangents = accumulator.biTangents.toArray
    )
  }

  private def emptyAccumulator = Accumulator(
    Vector.empty,
    Vector.empty,
    Vector.empty,
    Vector.empty,
    Vector.empty
  )

  private final case class Accumulator(
      vertices: Vector[Float],
      textures: Vector[Float],
      normals: Vector[Float],
      tangents: Vector[Float],
      biTangents: Vector[Float]
  )
}
