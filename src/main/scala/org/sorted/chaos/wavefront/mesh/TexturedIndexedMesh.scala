package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.{ Indices, Point, UVCoordinate, Wavefront }

/**
  * This model class represents a Mesh with vertices, a texture and a index Array for indexed drawing
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject
  * @param indexes the array of index for the IndexBufferObject
  */
final case class TexturedIndexedMesh(vertices: Array[Float], textures: Array[Float], indexes: Array[Int])

object TexturedIndexedMesh {

  private final case class Accumulator(
      currentIndex: Int,
      lookUpTable: Map[Indices, Int],
      vertices: Vector[Point],
      textures: Vector[UVCoordinate],
      indexes: Vector[Int]
  )

  private def emptyAccumulator =
    Accumulator(
      0,
      Map.empty[Indices, Int],
      Vector.empty[Point],
      Vector.empty[UVCoordinate],
      Vector.empty[Int]
    )

  def from(wavefront: Wavefront): TexturedIndexedMesh = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTextures  = wavefront.textures
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
            indexes      = accumulator.indexes :+ accumulator.currentIndex
          )
        }
      }
    }

    val vertices = result.vertices.flatMap(_.toArray).toArray
    val textures = result.textures.flatMap(_.toArray).toArray
    val indexes  = result.indexes.toArray

    TexturedIndexedMesh(vertices, textures, indexes)
  }
}
