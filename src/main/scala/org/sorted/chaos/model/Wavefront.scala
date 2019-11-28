package org.sorted.chaos.model

import org.slf4j.LoggerFactory

import scala.util.{ Failure, Success, Try }

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

final case class WavefrontUnvalidated(
    vertices: Vector[Point],
    triangles: Vector[Triangle],
    normals: Vector[Point],
    textures: Vector[UVCoordinate],
    smoothShading: Boolean,
    errors: Vector[String]
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

  private val EmptyWavefrontUnvalidated = WavefrontUnvalidated(
    Vector.empty[Point],
    Vector.empty[Triangle],
    Vector.empty[Point],
    Vector.empty[UVCoordinate],
    false,
    Vector.empty[String]
  )

  private[model] def empty =
    Wavefront(
      vertices      = Vector.empty[Point],
      triangles     = Vector.empty[Triangle],
      normals       = Vector.empty[Point],
      textures      = Vector.empty[UVCoordinate],
      smoothShading = false
    )

  def x(unvalidated: WavefrontUnvalidated): Try[Wavefront] =
    if (unvalidated.errors.nonEmpty) {
      val errorMsg =
        s"""|The following error(s) occurred during reading the .obj file:
            |${unvalidated.errors.mkString("\n")}
           """.stripMargin
      Failure(new IllegalArgumentException(errorMsg))
    } else {
      Success(
        Wavefront(
          vertices      = unvalidated.vertices,
          triangles     = unvalidated.triangles,
          normals       = unvalidated.normals,
          textures      = unvalidated.textures,
          smoothShading = unvalidated.smoothShading
        )
      )
    }

  def from(lines: Vector[String]): WavefrontUnvalidated =
    lines.foldLeft(EmptyWavefrontUnvalidated) { (accWavefront, line) =>
      {
        val token = line.take(2).trim
        token match {
          case Vertex =>
            val point = createPointFrom(line)
            point match {
              case Left(error) =>
                accWavefront.copy(errors = accWavefront.errors :+ error)
              case Right(p) =>
                accWavefront.copy(vertices = accWavefront.vertices :+ p)
            }
          case Face =>
            val triangle = createTriangleFrom(line)
            triangle match {
              case Left(error) =>
                accWavefront.copy(errors = accWavefront.errors :+ error)
              case Right(tri) =>
                accWavefront.copy(triangles = accWavefront.triangles :+ tri)
            }
          case Texture =>
            val uvCoordinate = createUVCoordinateFrom(line)
            uvCoordinate match {
              case Left(error) =>
                accWavefront.copy(errors = accWavefront.errors :+ error)
              case Right(tex) =>
                accWavefront.copy(textures = accWavefront.textures :+ tex)
            }
          case Normal =>
            val point = createPointFrom(line)
            point match {
              case Left(error) =>
                accWavefront.copy(errors = accWavefront.errors :+ error)
              case Right(nor) =>
                accWavefront.copy(normals = accWavefront.normals :+ nor)
            }
          case SmoothShading =>
            val smoothShading = isSmoothShading(line)
            smoothShading match {
              case Left(error) =>
                accWavefront.copy(errors = accWavefront.errors :+ error)
              case Right(smo) =>
                accWavefront.copy(smoothShading = smo)
            }
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

    if (indexes.length == 3) {
      Right(
        Triangle(
          indexes(0),
          indexes(1),
          indexes(2)
        )
      )
    } else {
      Left("alalal")
    }
  }

  private def isSmoothShading(line: String) = {
    val lineParts = line.split(Space)
    if (lineParts.length != 2) {
      Left(
        s"  * There are 2 arguments [token value] needed to parse the smooth-group, but ${lineParts.length} was/were found (source: '$line')."
      )
    } else {
      lineParts(1) match {
        case "1"   => Right(true)
        case "off" => Right(false)
        case x =>
          Left(s"  * The only values for a smooth-group are '1' and 'off', but '$x' was found.")
      }
    }
  }

  private def createPointFrom(line: String) = {
    val lineParts = line.split(Space)
    if (lineParts.length != 4) {
      Left(
        s"  * There are 4 arguments [token number number number] needed to parse a vertex/normal coordinate, but ${lineParts.length} was/were found (source: '$line')."
      )
    } else {
      val numbers = lineParts.tail.flatMap(_.toFloatOption)
      if (numbers.length != 3) {
        Left(
          s"  * There are 3 numbers needed to parse a vertex/normal coordinate, but something could not transformed to a Float (source: '${lineParts.tail.mkString}')."
        )
      } else {
        Right(Point(numbers(0), numbers(1), numbers(2)))
      }
    }
  }

  private def createUVCoordinateFrom(line: String) = {
    val lineParts = line.split(Space)
    if (lineParts.length != 3) {
      Left(
        s"  * There are 3 arguments [token number number] needed to parse a texture coordinate, but only ${lineParts.length} was/were found (source: '$line')."
      )
    } else {
      val numbers = lineParts.tail.flatMap(_.toFloatOption)
      if (numbers.length != 2) {
        Left(
          s"  * There are 2 numbers needed to parse a texture coordinate, but something could not transformed to a Float (source: '${lineParts.tail.mkString})'."
        )
      } else {
        Right(UVCoordinate(numbers(0), numbers(1)))
      }
    }
  }
}
