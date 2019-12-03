package org.sorted.chaos.wavefront.reader

import org.sorted.chaos.wavefront.reader.Wavefront.Space

/**
  * This model class represents indices (vertex, texture, normal) for one point of a triangle.
  *
  * @param vertexIndex the index of the vertex
  * @param textureIndex the index of the texture (optional)
  * @param normalIndex the index of the normal (optional)
  */
final case class Indices(vertexIndex: Int, textureIndex: Option[Int], normalIndex: Option[Int])

/**
  *  This model class represents the Triangle definition of the .obj file, starting with `f ...`.
  *  There are several abilities.
  *  Examples:
  *  * `f 7 6 8` - only vertices
  *  * `f 1/1 2/2 3/3 4/4` - vertex & texture
  *  * `f 1//3 4//5 2//8` - vertex & normals
  *  * `f 28/28/28 79/29/29 78/27/27` - vertex, texture & normal
  *
  * @param point1 The indices for the first point of the triangle
  * @param point2 The indices for the second point of the triangle
  * @param point3 The indices for the third point of the triangle
  */
final case class Triangle(point1: Indices, point2: Indices, point3: Indices) {

  def asVector: Vector[Indices] = Vector(point1, point2, point3)
}

object Triangle {
  private final val VertexTextureNormalPattern = """(\d+)/(\d+)/(\d+)""".r
  private final val VertexTexturePattern       = """(\d+)/(\d+)""".r
  private final val VertexNormalPattern        = """(\d+)//(\d+)""".r
  private final val VertexPattern              = """(\d+)""".r

  def from(line: String): Either[String, Triangle] = {
    val lineParts = line.split(Space)
    val trianglePoints = lineParts.tail.map {
      case VertexTextureNormalPattern(vertex, texture, normal) =>
        createIndicesFromVertexTextureNormalPattern(vertex, texture, normal)
      case VertexTexturePattern(vertex, texture) =>
        createIndicesFromVertexTexturePattern(vertex, texture)
      case VertexNormalPattern(vertex, normal) =>
        createIndicesFromVertexNormalPattern(vertex, normal)
      case VertexPattern(vertex) =>
        createIndicesFromVertexPattern(vertex)
      case _ => Left(s"  * ParseError for Triangle definition - no pattern found for '$line'")
    }

    if (lineParts.length != 4) {
      Left(
        "  * ParseError for Triangle definition - pattern consists of 4 arguments [token indices indices indices], but " +
        s"${lineParts.length} argument(s) was/were found (source was: '$line')."
      )
    } else if (trianglePoints(0).isLeft || trianglePoints(1).isLeft || trianglePoints(2).isLeft) {
      val lefts        = trianglePoints.filter(item => item.isLeft)
      val errorMessage = lefts.flatMap(item         => item.swap.toSeq)
      Left(errorMessage.mkString("\n"))
    } else {
      val index = trianglePoints.flatMap(item => item.toSeq)
      Right(
        Triangle(
          index(0),
          index(1),
          index(2)
        )
      )
    }
  }

  private def createIndicesFromVertexTextureNormalPattern(vertex: String, texture: String, normal: String) =
    Right(
      Indices(
        vertexIndex  = vertex.toInt,
        textureIndex = Some(texture.toInt),
        normalIndex  = Some(normal.toInt)
      )
    )

  private def createIndicesFromVertexTexturePattern(vertex: String, texture: String) =
    Right(
      Indices(
        vertexIndex  = vertex.toInt,
        textureIndex = Some(texture.toInt),
        normalIndex  = None
      )
    )

  private def createIndicesFromVertexNormalPattern(vertex: String, normal: String) =
    Right(
      Indices(
        vertexIndex  = vertex.toInt,
        textureIndex = None,
        normalIndex  = Some(normal.toInt)
      )
    )

  private def createIndicesFromVertexPattern(vertex: String) =
    Right(
      Indices(
        vertexIndex  = vertex.toInt,
        textureIndex = None,
        normalIndex  = None
      )
    )
}
