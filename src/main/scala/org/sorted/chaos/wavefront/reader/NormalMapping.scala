package org.sorted.chaos.wavefront.reader

import org.joml.{ Vector2f, Vector3f }

final case class NormalMappingPoint(vertex: Vector3f, texture: Vector2f)

final case class NormalMappingTriangle(p0: NormalMappingPoint, p1: NormalMappingPoint, p2: NormalMappingPoint)

// http://ogldev.atspace.co.uk/www/tutorial26/tutorial26.html
// http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-13-normal-mapping/
// https://learnopengl.com/Advanced-Lighting/Normal-Mapping
object NormalMapping {
  def generateFrom(triangle: NormalMappingTriangle): (Vector3f, Vector3f) = {

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
}
