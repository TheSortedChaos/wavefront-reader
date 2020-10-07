package de.sorted.chaos.wavefront.mesh

import de.sorted.chaos.wavefront.reader.{Indices, Wavefront}
import org.joml.{Vector2f, Vector3f}

/**
  * This model class represents a [[IndexMesh]] with vertices, texture, normals, and an index list
  *
  * @param vertices the array of vertices for the VertexBufferObject
  * @param textures the array of UV coordinates for the VertexBufferObject, will be empty, if no data for this type was found
  * @param normals the array of normals for the VertexBufferObject, will be empty, if no data for this type was found
  * @param tangents the array of tangents for the VertexBufferObject (for NormalMapping),
  *                 will be empty, if no data for this type was found
  * @param biTangents the array of biTangents for the VertexBufferObject (forNormalMapping),
  *                   will be empty, if no data for this type was found
  * @param indexes the array of the indexes (used for OpenGL index drawing)
  */
final case class IndexMesh(
    vertices: Array[Float],
    textures: Array[Float],
    normals: Array[Float],
    tangents: Array[Float],
    biTangents: Array[Float],
    indexes: Array[Int]
)

object IndexMesh {
  import de.sorted.chaos.wavefront.reader.JomlExtension.{Vector2fExtension, Vector3fExtension}

  def from(wavefront: Wavefront): IndexMesh = {
    val wavefrontVertices   = wavefront.vertices
    val wavefrontTextures   = wavefront.textures
    val wavefrontNormals    = wavefront.normals
    val wavefrontTangents   = wavefront.tangents
    val wavefrontBiTangents = wavefront.biTangents
    val wavefrontTriangles  = wavefront.triangles

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
            tangents     = addTangents(indices, accumulator, wavefrontTangents),
            biTangents   = addBiTangents(indices, accumulator, wavefrontBiTangents),
            indexes      = accumulator.indexes :+ accumulator.currentIndex
          )
        }
      }
    }

    val vertices   = result.vertices.flatMap(_.toVector).toArray
    val textures   = result.textures.flatMap(_.toVector).toArray
    val normals    = result.normals.flatMap(_.toVector).toArray
    val tangents   = result.tangents.flatMap(_.toVector).toArray
    val biTangents = result.biTangents.flatMap(_.toVector).toArray
    val indexes    = result.indexes.toArray

    IndexMesh(
      vertices   = vertices,
      textures   = textures,
      normals    = normals,
      tangents   = tangents,
      biTangents = biTangents,
      indexes    = indexes
    )
  }

  private def emptyAccumulator =
    Accumulator(
      0,
      Map.empty[Indices, Int],
      Vector.empty[Vector3f],
      Vector.empty[Vector2f],
      Vector.empty[Vector3f],
      Vector.empty[Vector3f],
      Vector.empty[Vector3f],
      Vector.empty[Int]
    )

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

  private def addTangents(indices: Indices, accumulator: Accumulator, wavefrontTangents: Vector[Vector3f]) =
    indices.tangentsIndex match {
      case None        => accumulator.tangents
      case Some(index) => accumulator.tangents :+ wavefrontTangents(index - 1)
    }

  private def addBiTangents(indices: Indices, accumulator: Accumulator, wavefrontBiTangents: Vector[Vector3f]) =
    indices.biTangentsIndex match {
      case None        => accumulator.biTangents
      case Some(index) => accumulator.biTangents :+ wavefrontBiTangents(index - 1)
    }

  private final case class Accumulator(
      currentIndex: Int,
      lookUpTable: Map[Indices, Int],
      vertices: Vector[Vector3f],
      textures: Vector[Vector2f],
      normals: Vector[Vector3f],
      tangents: Vector[Vector3f],
      biTangents: Vector[Vector3f],
      indexes: Vector[Int]
  )
}
