package org.sorted.chaos.wavefront.reader

import org.joml.{ Vector2f, Vector3f }
import org.scalatest.{ Matchers, WordSpec }

class NormalMappingTest extends WordSpec with Matchers {

  "Normal Mapping" should {
    "calculate the tangents and biTangents from a wavefront" in {
      val input = Wavefront(
        vertices = Vector(
          new Vector3f(1.0f, 1.0f, 0.0f),
          new Vector3f(4.0f, 1.0f, 0.0f),
          new Vector3f(4.0f, 4.0f, 0.0f),
          new Vector3f(1.0f, 4.0f, 0.0f)
        ),
        triangles = Vector(
          Triangle(
            Indices(1, Some(1), Some(1), None, None),
            Indices(2, Some(2), Some(1), None, None),
            Indices(3, Some(3), Some(1), None, None)
          ),
          Triangle(
            Indices(1, Some(1), Some(1), None, None),
            Indices(3, Some(3), Some(1), None, None),
            Indices(4, Some(4), Some(1), None, None)
          )
        ),
        normals = Vector(
          new Vector3f(0.0f, 0.0f, 1.0f)
        ),
        textures = Vector(
          new Vector2f(0.0f, 0.0f),
          new Vector2f(1.0f, 0.0f),
          new Vector2f(1.0f, 1.0f),
          new Vector2f(0.0f, 1.0f)
        ),
        tangents   = Vector.empty[Vector3f],
        biTangents = Vector.empty[Vector3f]
      )

      val actual = NormalMapping.calculateFrom(input)
      actual.tangents shouldBe Vector(
        new Vector3f(3.0f, 0.0f, 0.0f),
        new Vector3f(3.0f, 0.0f, 0.0f)
      )
      actual.biTangents shouldBe Vector(
        new Vector3f(-6.0f, -3.0f, -0.0f),
        new Vector3f(-0.0f, -0.0f, -0.0f)
      )
    }
  }
}
