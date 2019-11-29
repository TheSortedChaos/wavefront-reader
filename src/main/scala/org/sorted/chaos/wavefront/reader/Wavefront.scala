package org.sorted.chaos.wavefront.reader

import scala.util.{ Failure, Success, Try }

/**
  * This model class represents the .obj file, containing all the relevant data
  *
  * @param vertices a list of vertex definitions
  * @param triangles a list of triangle definitions
  * @param normals a list of normal definitions
  * @param textures a list of texture definitions
  * @param smoothShading one normal per triangle (false) or one normal per vertex (true)
  */
final case class Wavefront(
    vertices: Vector[Point],
    triangles: Vector[Triangle],
    normals: Vector[Point],
    textures: Vector[UVCoordinate],
    smoothShading: Boolean
)

private final case class WavefrontWithErrors(wavefront: Wavefront, errors: Vector[String])

object Wavefront {
  final val Space = " "

  private val Vertex        = "v"
  private val Face          = "f"
  private val Texture       = "vt"
  private val Normal        = "vn"
  private val SmoothShading = "s"

  private def empty =
    Wavefront(
      vertices      = Vector.empty[Point],
      triangles     = Vector.empty[Triangle],
      normals       = Vector.empty[Point],
      textures      = Vector.empty[UVCoordinate],
      smoothShading = false
    )

  def from(lines: Vector[String]): Try[Wavefront] = {
    val parseResult = lines.foldLeft(WavefrontWithErrors(Wavefront.empty, Vector.empty[String])) { (accumulator, line) =>
      {
        val token = line.take(2).trim
        token match {
          case Vertex =>
            val point = Point.from(line)
            parseVertex(accumulator, point)
          case Face =>
            val triangle = Triangle.from(line)
            parseTriangle(accumulator, triangle)
          case Texture =>
            val uvCoordinate = UVCoordinate.from(line)
            parseTexture(accumulator, uvCoordinate)
          case Normal =>
            val point = Point.from(line)
            parseNormal(accumulator, point)
          case SmoothShading =>
            val smoothShading = isSmoothShading(line)
            parseSmoothGroup(accumulator, smoothShading)
          case _ =>
            accumulator
        }
      }
    }

    if (parseResult.errors.nonEmpty) {
      val errorMessage =
        s"""|The following error(s) occurred during reading the .obj file:
            |${parseResult.errors.mkString("\n")}
         """.stripMargin
      Failure(new IllegalArgumentException(errorMessage))
    } else {
      Success(parseResult.wavefront)
    }
  }

  private def parseSmoothGroup(accumulator: WavefrontWithErrors, smoothShading: Either[String, Boolean]) =
    smoothShading match {
      case Left(error) =>
        accumulator.copy(errors = accumulator.errors :+ error)
      case Right(smo) =>
        val wavefront    = accumulator.wavefront
        val newWavefront = wavefront.copy(smoothShading = smo)
        accumulator.copy(wavefront = newWavefront)
    }

  private def parseNormal(accumulator: WavefrontWithErrors, point: Either[String, Point]) =
    point match {
      case Left(error) =>
        accumulator.copy(errors = accumulator.errors :+ error)
      case Right(nor) =>
        val wavefront    = accumulator.wavefront
        val newWavefront = wavefront.copy(normals = wavefront.normals :+ nor)
        accumulator.copy(wavefront = newWavefront)
    }

  private def parseTexture(accumulator: WavefrontWithErrors, uvCoordinate: Either[String, UVCoordinate]) =
    uvCoordinate match {
      case Left(error) =>
        accumulator.copy(errors = accumulator.errors :+ error)
      case Right(tex) =>
        val wavefront    = accumulator.wavefront
        val newWavefront = wavefront.copy(textures = wavefront.textures :+ tex)
        accumulator.copy(wavefront = newWavefront)
    }

  private def parseTriangle(accumulator: WavefrontWithErrors, triangle: Either[String, Triangle]) =
    triangle match {
      case Left(error) =>
        accumulator.copy(errors = accumulator.errors :+ error)
      case Right(tri) =>
        val wavefront    = accumulator.wavefront
        val newWavefront = wavefront.copy(triangles = wavefront.triangles :+ tri)
        accumulator.copy(wavefront = newWavefront)
    }

  private def parseVertex(accumulator: WavefrontWithErrors, point: Either[String, Point]) =
    point match {
      case Left(error) =>
        accumulator.copy(errors = accumulator.errors :+ error)
      case Right(p) =>
        val wavefront    = accumulator.wavefront
        val newWavefront = wavefront.copy(vertices = wavefront.vertices :+ p)
        accumulator.copy(wavefront = newWavefront)
    }

  private def isSmoothShading(line: String) = {
    val lineParts = line.split(Space)
    if (lineParts.length != 2) {
      Left(
        "  * There are 2 arguments [token value] needed to parse the smooth-group, " +
        s"but ${lineParts.length} was/were found (source: '$line')."
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
}
