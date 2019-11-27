package org.sorted.chaos.model

import org.slf4j.LoggerFactory

/**
  * This model describes each index for one point of a triangle.
  *
  * @param vertexIndex the index of the vertex
  * @param textureIndex the index of the texture (optional)
  * @param normalIndex the index of the normal (optional)
  */
final case class Index(vertexIndex: Int, textureIndex: Option[Int], normalIndex: Option[Int])

/**
  *  This model describes the face (triangle) definition of the .obj file starting with `f ...`. There are
  *  several abilities possible
  *  Examples:
  *  * `f 7 6 8` - only vertices
  *  * f 1/1 2/2 3/3 4/4 - vertex & texture
  *  * `f 1//3 4//5 2//8` - vertex & normals
  *  * `f 28/28/28 79/29/29 78/27/27` - vertex, texture & normal
  *
  * @param index1 The indexes for the first point of the triangle
  * @param index2 The indexes for the second point of the triangle
  * @param index3 The indexes for the third point of the triangle
  */
final case class Triangle(index1: Index, index2: Index, index3: Index) {
  def asVector: Vector[Index] = Vector(index1, index2, index3)
}

/**
  * This model describes the vertex/normal definition of the .obj file starting with `v ...` or `vn ...`
  * Example
  * * `v 0.230970 -1.000000 0.095671` (vertex)
  * or
  * * `vn 0.6894 -0.6657 0.2855` (normal)
  * @param x coordinate
  * @param y coordinate
  * @param z coordinate
  */
final case class Point(x: Float, y: Float, z: Float) {
  def toArray: Array[Float] = Array(x, y, z)
}

/**
  * This model describes the texture definition of the .obj file starting with `vt ...`
  * Example:
  * * `vt 0.609836 0.758661`
  * @param u texture coordinate
  * @param v texture coordinate
  */
final case class UVCoordinate(u: Float, v: Float)

/**
  * This is the internal model of the .obj file, containing all the relevant data
  *
  * @param vertices a list of vertex definitions
  * @param triangles a list of triangle definitions
  * @param normals a list of normal definitions
  * @param textures a list of texture definitions
  */
final case class Wavefront(
    vertices: Vector[Point],
    triangles: Vector[Triangle],
    normals: Vector[Point],
    textures: Vector[UVCoordinate],
    smoothShading: Boolean
)

object Wavefront {
  private val Logger = LoggerFactory.getLogger(this.getClass)

  private val Vertex        = "v"
  private val Face          = "f"
  private val Texture       = "vt"
  private val Normal        = "vn"
  private val Space         = " "
  private val SmoothShading = "s"

  private val VertexTextureNormalPattern = """(\d+)/(\d+)/(\d+)""".r
  private val VertexTexturePattern       = """(\d+)/(\d+)""".r
  private val VertexNormalPattern        = """(\d+)//(\d+)""".r
  private val VertexPattern              = """(\d+)""".r

  private[model] def empty =
    Wavefront(
      vertices      = Vector.empty[Point],
      triangles     = Vector.empty[Triangle],
      normals       = Vector.empty[Point],
      textures      = Vector.empty[UVCoordinate],
      smoothShading = false
    )

  def from(lines: Vector[String]): Wavefront =
    lines.foldLeft(Wavefront.empty) { (accWavefront, line) =>
      {
        val token = line.take(2).trim
        token match {
          case Vertex =>
            val point = createPointFrom(line)
            accWavefront.copy(vertices = accWavefront.vertices :+ point)
          case Face =>
            val triangle = createTriangleFrom(line)
            accWavefront.copy(triangles = accWavefront.triangles :+ triangle)
          case Texture =>
            val uvCoordinate = createUVCoordinateFrom(line)
            accWavefront.copy(textures = accWavefront.textures :+ uvCoordinate)
          case Normal =>
            val point = createPointFrom(line)
            accWavefront.copy(normals = accWavefront.normals :+ point)
          case SmoothShading =>
            val smoothShading = isSmoothShading(line)
            accWavefront.copy(smoothShading = smoothShading)
          case _ =>
            accWavefront
        }
      }
    }

  // TODO refactor + Error handling
  private def createTriangleFrom(line: String) = {
    val parts = line.split(Space).tail
    val indexes = parts.map {
      case VertexTextureNormalPattern(vertex, texture, normal) =>
        Index(
          vertexIndex  = vertex.toInt,
          textureIndex = Some(texture.toInt),
          normalIndex  = Some(normal.toInt)
        )
      case VertexTexturePattern(vertex, texture) =>
        Index(
          vertexIndex  = vertex.toInt,
          textureIndex = Some(texture.toInt),
          normalIndex  = None
        )
      case VertexNormalPattern(vertex, normal) =>
        Index(
          vertexIndex  = vertex.toInt,
          textureIndex = None,
          normalIndex  = Some(normal.toInt)
        )
      case VertexPattern(vertex) =>
        Index(
          vertexIndex  = vertex.toInt,
          textureIndex = None,
          normalIndex  = None
        )
    }

    Triangle(
      indexes(0),
      indexes(1),
      indexes(2)
    )
  }

  private def isSmoothShading(line: String) = {
    val lineParts = line.split(Space)
    if (lineParts(1) == "1") {
      true
    } else {
      false
    }
  }

  private def createPointFrom(line: String) = {
    val numbers = extractFloatsFrom(line)
    Point(numbers(0), numbers(1), numbers(2))
  }

  private def createUVCoordinateFrom(line: String) = {
    val numbers = extractFloatsFrom(line)
    UVCoordinate(numbers(0), numbers(1))
  }

  private def extractFloatsFrom(line: String) = {
    val lineParts = line.split(Space)
    lineParts.tail.map(_.toFloat)
  }
}
