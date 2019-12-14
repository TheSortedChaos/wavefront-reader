package org.sorted.chaos.wavefront.reader

/**
  * This model class represents the .mtl file, containing all relevant data
  *
  * @param ambientColor The ambient color (rgb)
  * @param diffuseColor The diffuse color (rgb)
  * @param specularColor the specular color (rgb)
  * @param specularExponent the specular exponent (between 0 and 1000)
  * @param alphaChannel the alpha channel (between 0 and 1)
  */
final case class Material(
    ambientColor: Color,
    diffuseColor: Color,
    specularColor: Color,
    specularExponent: Float,
    alphaChannel: Float
)

object Material {
  import Color._
  import ExtractSingleFloat._

  private final val Ambient  = "Ka"
  private final val Diffuse  = "Kd"
  private final val Specular = "Ks"
  private final val Exponent = "Ns"
  private final val Alpha    = "d"

  def empty(): Material = Material(
    ambientColor     = Color(0.0f, 0.0f, 0.0f),
    diffuseColor     = Color(0.0f, 0.0f, 0.0f),
    specularColor    = Color(0.0f, 0.0f, 0.0f),
    specularExponent = 0.0f,
    alphaChannel     = 1.0f
  )

  def from(lines: Vector[String]): Material =
    lines.foldLeft(Material.empty()) { (accumulator, line) =>
      {
        val token = extractToken(line)
        processLine(accumulator, line, token)
      }
    }

  private def extractToken(line: String): String =
    line.take(2).trim

  private def processLine(accumulator: Material, line: String, token: String) =
    token match {
      case Ambient  => accumulator.copy(ambientColor = line.getColor)
      case Diffuse  => accumulator.copy(diffuseColor = line.getColor)
      case Specular => accumulator.copy(specularColor = line.getColor)
      case Exponent => accumulator.copy(specularExponent = line.getSpecularExponent)
      case Alpha    => accumulator.copy(alphaChannel = line.getAlphaChannel)
      case _        => accumulator
    }
}
