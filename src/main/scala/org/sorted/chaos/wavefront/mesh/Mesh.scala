package org.sorted.chaos.wavefront.mesh

import org.joml.{Vector2f, Vector3f}
import org.sorted.chaos.wavefront.reader.{NormalMapping, NormalMappingPoint, NormalMappingTriangle, Wavefront}
import org.sorted.chaos.wavefront.utilities.Timer

import scala.annotation.tailrec

/**
  * This model class represents a [[Mesh]] with vertices, texture and normals, but without a index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject, will be empty, if no data for this type was found
  * @param normals the array of normals for the VertexBufferObject, will be empty, if no data for this type was found
  */
final case class Mesh(
    vertices: Array[Float],
    textures: Array[Float],
    normals: Array[Float],
    tangents: Array[Float],
    biTangents: Array[Float]
)

object Mesh extends Geometry {

  private def empty = Mesh(
    vertices   = Array.emptyFloatArray,
    textures   = Array.emptyFloatArray,
    normals    = Array.emptyFloatArray,
    tangents   = Array.emptyFloatArray,
    biTangents = Array.emptyFloatArray
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
          vertices   = accumulator.vertices ++ vertices,
          textures   = accumulator.textures ++ textures,
          normals    = accumulator.normals ++ normals,
          tangents   = Array.emptyFloatArray,
          biTangents = Array.emptyFloatArray
        )
      }
    }
  }

  // scalastyle:off magic.number
  // scalastyle:off method.length
  def normalMapping(mesh: Mesh): Mesh = {
    val t1 = Timer.start
    @tailrec
    def helper(
        vertices: Array[Float],
        textures: Array[Float],
        accumulator: (Array[Float], Array[Float])
    ): (Array[Float], Array[Float]) =
      if (vertices.isEmpty || textures.isEmpty) {
        accumulator
      } else {
        val vertexCoords  = vertices.take(9)
        val textureCoords = textures.take(6)

        val restVertices = vertices.drop(9)
        val restTextures = textures.drop(6)

        val vertexP0 = new Vector3f(vertexCoords(0), vertexCoords(1), vertexCoords(2))
        val vertexP1 = new Vector3f(vertexCoords(3), vertexCoords(4), vertexCoords(5))
        val vertexP2 = new Vector3f(vertexCoords(6), vertexCoords(7), vertexCoords(8))

        val textureP0 = new Vector2f(textureCoords(0), textureCoords(1))
        val textureP1 = new Vector2f(textureCoords(2), textureCoords(3))
        val textureP2 = new Vector2f(textureCoords(4), textureCoords(5))

        val p0 = NormalMappingPoint(vertexP0, textureP0)
        val p1 = NormalMappingPoint(vertexP1, textureP1)
        val p2 = NormalMappingPoint(vertexP2, textureP2)

        val nmTriangle = NormalMappingTriangle(p0, p1, p2)

        val result    = NormalMapping.generateFrom(nmTriangle)
        val tangent   = Array(result._1.x, result._1.y, result._1.z)
        val biTangent = Array(result._2.x, result._2.y, result._2.z)

        val newAccumulator =
          ((accumulator._1 :++ tangent :++ tangent :++ tangent), (accumulator._2 :++ biTangent :++ biTangent :++ biTangent))
        helper(restVertices, restTextures, newAccumulator)
      }

    val result = helper(
      mesh.vertices,
      mesh.textures,
      (Array.emptyFloatArray, Array.emptyFloatArray)
    )

    Timer.end(t1, "Create Normal Mapping")
    Mesh(
      vertices   = mesh.vertices,
      textures   = mesh.textures,
      normals    = mesh.normals,
      tangents   = result._1,
      biTangents = result._2
    )
  }
  // scalastyle:on magic.number
  // scalastyle:on method.length
}
