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
  private val VertexTextureNormalPattern = """(\d+)/(\d+)/(\d+)""".r
  private val VertexTexturePattern       = """(\d+)/(\d+)""".r
  private val VertexNormalPattern        = """(\d+)//(\d+)""".r
  private val VertexPattern              = """(\d+)""".r

  def from(line: String): Either[String, Triangle] = {
    val lineParts = line.split(Space)
    val trianglePoints = lineParts.tail.map {
      case VertexTextureNormalPattern(vertex, texture, normal) =>
        createIndicesFromVertexTextureNormalPattern(vertex, texture, normal, line)
      case VertexTexturePattern(vertex, texture) =>
        createIndicesFromVertexTexturePattern(vertex, texture, line)
      case VertexNormalPattern(vertex, normal) =>
        createIndicesFromVertexNormalPattern(vertex, normal, line)
      case VertexPattern(vertex) =>
        createIndicesFromVertexPattern(vertex, line)
      case _ => Left(s"  * ParseError for Triangle definition - no pattern found for '$line'")
    }

    if (lineParts.length != 4) {
      Left(
        "  * ParseError for Triangle definition - pattern consists of 4 arguments [token indices indices indices], but " +
        s"${line.length} argument(s) was/were found (source was: '$line')."
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

  private def createIndicesFromVertexTextureNormalPattern(vertex: String, texture: String, normal: String, line: String) =
    (vertex.toIntOption, texture.toIntOption, normal.toIntOption) match {
      case (None, None, None) =>
        Left(
          "  * ParseError inside Triangle definition [f v/vt/vn v/vt/vn v/vt/vn] VertexIndex, TextureIndex and NormalIndex " +
          "could not transformed to an Int " +
          s"(source was: VertexIndex='$vertex' TextureIndex='$texture' NormalIndex='$normal' line was: '$line')."
        )
      case (Some(_), None, None) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt/vn v/vt/vn v/vt/vn] TextureIndex and NormalIndex could not " +
          s"transformed to an Int (source was: TextureIndex='$texture' NormalIndex='$normal' line was: '$line')."
        )
      case (None, Some(_), None) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt/vn v/vt/vn v/vt/vn] VertexIndex and NormalIndex could not " +
          s"transformed to an Int (source was: VertexIndex='$vertex' NormalIndex='$normal' line was: '$line')."
        )
      case (None, None, Some(_)) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt/vn v/vt/vn v/vt/vn] VertexIndex and TextureIndex could not " +
          s"transformed to an Int (source was: VertexIndex='$vertex' TextureIndex='$texture' line was: '$line')."
        )
      case (Some(_), Some(_), None) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt/vn v/vt/vn v/vt/vn] NormalIndex could not " +
          s"transformed to an Int (source was: NormalIndex='$normal' line was: '$line')."
        )
      case (Some(_), None, Some(_)) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt/vn v/vt/vn v/vt/vn] TextureIndex could not " +
          s"transformed to an Int (source was: TextureIndex='$texture' line was: '$line')."
        )
      case (None, Some(_), Some(_)) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt/vn v/vt/vn v/vt/vn] VertexIndex could not " +
          s"transformed to an Int (source was: VertexIndex='$vertex' line was: '$line')."
        )
      case (Some(v), Some(t), Some(n)) =>
        Right(
          Indices(
            vertexIndex  = v,
            textureIndex = Some(t),
            normalIndex  = Some(n)
          )
        )
    }

  private def createIndicesFromVertexTexturePattern(vertex: String, texture: String, line: String) =
    (vertex.toIntOption, texture.toIntOption) match {
      case (None, None) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt v/vt v/vt] VertexIndex and TextureIndex could not " +
          s"transformed to an Int (source was: VertexIndex='$vertex' TextureIndex='$texture' line was: '$line')."
        )
      case (Some(_), None) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt v/vt v/vt] TextureIndex could not " +
          s"transformed to an Int (source was: TextureIndex='$texture' line was: '$line')."
        )
      case (None, Some(_)) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v/vt v/vt v/vt] VertexIndex could not " +
          s"transformed to an Int (source was: VertexIndex='$vertex' line was: '$line')."
        )
      case (Some(v), Some(t)) =>
        Right(
          Indices(
            vertexIndex  = v,
            textureIndex = Some(t),
            normalIndex  = None
          )
        )
    }

  private def createIndicesFromVertexNormalPattern(vertex: String, normal: String, line: String) =
    (vertex.toIntOption, normal.toIntOption) match {
      case (None, None) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v//vn v//vn v//vn] VertexIndex and NormalIndex could not " +
          s"transformed to an Int (source was: VertexIndex='$vertex' NormalIndex='$normal' line was: '$line')."
        )
      case (Some(_), None) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v//vn v//vn v//vn] NormalIndex could not " +
          s"transformed to an Int (source was: NormalIndex='$normal' line was: '$line')."
        )
      case (None, Some(_)) =>
        Left(
          "  * ParseError inside TriangleDefinition [f v//vn v//vn v//vn] VertexIndex could not " +
          s"transformed to an Int (source was: VertexIndex='$vertex' line was: '$line')."
        )
      case (Some(v), Some(n)) =>
        Right(
          Indices(
            vertexIndex  = v,
            textureIndex = None,
            normalIndex  = Some(n)
          )
        )
    }

  private def createIndicesFromVertexPattern(vertex: String, line: String) =
    vertex.toIntOption match {
      case None =>
        Left(
          "  * ParseError inside TriangleDefinition [f v v v] VertexIndex could not " +
          s"transformed to an Int (source was: VertexIndex='$vertex' line was: '$line')."
        )
      case Some(value) =>
        Right(
          Indices(
            vertexIndex  = value,
            textureIndex = None,
            normalIndex  = None
          )
        )
    }
}
