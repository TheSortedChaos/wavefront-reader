package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.{ Indices, Point, UVCoordinate, Wavefront }

/**
  * This model class represents a [[IndexMesh]] with vertices, texture, normals, and an index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject, will be empty, if no data for this type was found
  * @param normals the array of normals for the VertexBufferObject, will be empty, if no data for this type was found
  * @param indexes the array of the indexes (used for OpenGL index drawing)
  */
final case class IndexMesh(vertices: Array[Float], textures: Array[Float], normals: Array[Float], indexes: Array[Int])

object IndexMesh {

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

  def from(wavefront: Wavefront): IndexMesh = {
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
            textures     = addTextures(indices, accumulator, wavefrontTextures),
            normals      = addNormals(indices, accumulator, wavefrontNormals),
            indexes      = accumulator.indexes :+ accumulator.currentIndex
          )
        }
      }
    }

    val vertices = result.vertices.flatMap(_.toArray).toArray
    val textures = result.textures.flatMap(_.toArray).toArray
    val normals  = result.normals.flatMap(_.toArray).toArray
    val indexes  = result.indexes.toArray

    IndexMesh(vertices, textures, normals, indexes)
  }

  private def addTextures(indices: Indices, accumulator: Accumulator, wavefrontTextures: Vector[UVCoordinate]) =
    indices.textureIndex match {
      case None        => accumulator.textures
      case Some(index) => accumulator.textures :+ wavefrontTextures(index - 1)
    }

  private def addNormals(indices: Indices, accumulator: Accumulator, wavefrontNormals: Vector[Point]) =
    indices.normalIndex match {
      case None        => accumulator.normals
      case Some(index) => accumulator.normals :+ wavefrontNormals(index - 1)
    }
}
