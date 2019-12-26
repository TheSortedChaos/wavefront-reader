package org.sorted.chaos.wavefront.mesh

import org.joml.{ Vector2f, Vector3f }
import org.sorted.chaos.wavefront.reader.{ Indices, Wavefront }

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
  import org.sorted.chaos.wavefront.reader.JomlExtension.{ Vector2fExtension, Vector3fExtension }

  private final case class Accumulator(
      currentIndex: Int,
      lookUpTable: Map[Indices, Int],
      vertices: Vector[Vector3f],
      textures: Vector[Vector2f],
      normals: Vector[Vector3f],
      indexes: Vector[Int]
  )

  private def emptyAccumulator =
    Accumulator(
      0,
      Map.empty[Indices, Int],
      Vector.empty[Vector3f],
      Vector.empty[Vector2f],
      Vector.empty[Vector3f],
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

    val vertices = result.vertices.flatMap(_.toVector).toArray
    val textures = result.textures.flatMap(_.toVector).toArray
    val normals  = result.normals.flatMap(_.toVector).toArray
    val indexes  = result.indexes.toArray

    IndexMesh(vertices, textures, normals, indexes)
  }

  private def addTextures(indices: Indices, accumulator: Accumulator, wavefrontTextures: Vector[Vector2f]) =
    indices.textureIndex match {
      case None        => accumulator.textures
      case Some(index) => accumulator.textures :+ wavefrontTextures(index - 1)
    }

  private def addNormals(indices: Indices, accumulator: Accumulator, wavefrontNormals: Vector[Vector3f]) =
    indices.normalIndex match {
      case None        => accumulator.normals
      case Some(index) => accumulator.normals :+ wavefrontNormals(index - 1)
    }
}
