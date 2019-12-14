package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.{ Indices, Point, UVCoordinate, Wavefront }

/**
  * This model class represents a Mesh with vertices, a texture and a index Array for indexed drawing
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject
  * @param normals the array of normals for the VertexBufferObject
  * @param indexes the array of index for the IndexBufferObject
  */
final case class TexturedNormalIndexedMesh(
    vertices: Array[Float],
    textures: Array[Float],
    normals: Array[Float],
    indexes: Array[Int]
)

object TexturedNormalIndexedMesh {

  private final case class Accumulator(
      currentIndex: Int,
      lookUpTable: Map[Indices, Int],
      vertices: Vector[Point],
      textures: Vector[UVCoordinate],
      normals: Vector[Point],
      indexes: Vector[Int]
  )

  private def emptyAccumulator =
    Accumulator(
      0,
      Map.empty[Indices, Int],
      Vector.empty[Point],
      Vector.empty[UVCoordinate],
      Vector.empty[Point],
      Vector.empty[Int]
    )

  def from(wavefront: Wavefront): TexturedNormalIndexedMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTextures  = wavefront.textures
    val wavefrontNormals   = wavefront.normals
    val wavefrontTriangles = wavefront.triangles

    val allIndices = wavefrontTriangles.flatMap(_.indices)
    val result = allIndices.foldLeft(emptyAccumulator) { (accumulator, indices) =>
      {
        if (accumulator.lookUpTable.contains(indices)) {
          val newIndex = accumulator.lookUpTable(indices)
          accumulator.copy(indexes = accumulator.indexes :+ newIndex)
        } else {
          accumulator.copy(
            currentIndex = accumulator.currentIndex + 1,
            lookUpTable  = accumulator.lookUpTable + (indices -> accumulator.currentIndex),
            vertices     = accumulator.vertices :+ wavefrontVertices(indices.vertexIndex - 1),
            textures     = accumulator.textures :+ wavefrontTextures(indices.textureIndex.get - 1),
            normals      = accumulator.normals :+ wavefrontNormals(indices.normalIndex.get - 1),
            indexes      = accumulator.indexes :+ accumulator.currentIndex
          )
        }
      }
    }

    val vertices = result.vertices.flatMap(_.toArray).toArray
    val textures = result.textures.flatMap(_.toArray).toArray
    val normals  = result.normals.flatMap(_.toArray).toArray
    val indexes  = result.indexes.toArray

    TexturedNormalIndexedMesh(vertices, textures, normals, indexes)
  }
}
