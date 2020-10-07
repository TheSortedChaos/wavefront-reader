package de.sorted.chaos.wavefront.reader

import org.joml.{Vector2f, Vector3f}

final case class NormalMappingPoint(vertex: Vector3f, texture: Vector2f)

final case class NormalMappingTriangle(p0: NormalMappingPoint, p1: NormalMappingPoint, p2: NormalMappingPoint)

// http://ogldev.atspace.co.uk/www/tutorial26/tutorial26.html
// http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-13-normal-mapping/
// https://learnopengl.com/Advanced-Lighting/Normal-Mapping
// https://viscircle.de/welche-techniken-sie-fuer-das-normal-mapping-einsetzen-koennen/
object NormalMapping {
  def calculateFrom(wavefront: Wavefront): Wavefront = {
    val withNormalMapping = generateNormalMapping(wavefront)
    assignNormalMappingIndices(withNormalMapping)
  }

  private def generateNormalMapping(wavefront: Wavefront) = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTextures  = wavefront.textures
    val wavefrontTriangles = wavefront.triangles

    val tangentsAndBiTangents = wavefrontTriangles.foldLeft((Vector.empty[Vector3f], Vector.empty[Vector3f])) {
      (accumulator, triangle) =>
        {
          val nmPoints   = createNormalMappingPoints(wavefrontVertices, wavefrontTextures, triangle)
          val nmTriangle = NormalMappingTriangle(nmPoints(0), nmPoints(1), nmPoints(2))
          val result     = calculate(nmTriangle)

          accumulator.copy(
            _1 = accumulator._1 :+ result._1,
            _2 = accumulator._2 :+ result._2
          )
        }
    }

    Wavefront(
      vertices   = wavefront.vertices,
      textures   = wavefront.textures,
      normals    = wavefront.normals,
      triangles  = wavefront.triangles,
      tangents   = tangentsAndBiTangents._1,
      biTangents = tangentsAndBiTangents._2
    )
  }

  private def calculate(triangle: NormalMappingTriangle) = {
    val edge1 = new Vector3f(triangle.p1.vertex).sub(new Vector3f(triangle.p0.vertex))
    val edge2 = new Vector3f(triangle.p2.vertex).sub(new Vector3f(triangle.p0.vertex))

    val deltaU1 = triangle.p1.texture.x - triangle.p0.texture.x
    val deltaV1 = triangle.p1.texture.y - triangle.p0.texture.y
    val deltaU2 = triangle.p2.texture.x - triangle.p0.texture.x
    val deltaV2 = triangle.p2.texture.y - triangle.p0.texture.y

    val f = 1.0f / ((deltaU1 * deltaV2) - (deltaU2 * deltaV1))

    val tangent   = (new Vector3f(edge1).mul(deltaV2).sub(new Vector3f(edge2).mul(deltaV1))).mul(f)
    val biTangent = (new Vector3f(edge1).mul(-deltaU2).sub(new Vector3f(edge2).mul(deltaU2))).mul(f)

    (tangent, biTangent)
  }

  private def createNormalMappingPoints(
      wavefrontVertices: Vector[Vector3f],
      wavefrontTextures: Vector[Vector2f],
      triangle: Triangle
  ) =
    triangle.indices.foldLeft(Vector.empty[NormalMappingPoint]) { (accumulator, index) =>
      {
        val vertex  = wavefrontVertices(index.vertexIndex - 1)
        val texture = wavefrontTextures(index.textureIndex.get - 1)
        val nmPoint = NormalMappingPoint(vertex, texture)
        accumulator :+ nmPoint
      }
    }

  private def assignNormalMappingIndices(wavefront: Wavefront) = {
    val assigned = wavefront.triangles.zipWithIndex.map {
      case (triangle, index) => triangle.addNormalMappingIndex(index + 1, index + 1) // add +1 because wavefront index starts at 1
    }
    wavefront.copy(triangles = assigned)
  }
}
