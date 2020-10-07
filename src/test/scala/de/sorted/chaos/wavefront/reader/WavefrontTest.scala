package de.sorted.chaos.wavefront.reader

import org.joml.{ Vector2f, Vector3f }
import org.scalatest.{ Matchers, WordSpec }

class WavefrontTest extends WordSpec with Matchers {

  "A Wavefront" should {
    "handle broken lines of an .obj file" in {
      val input = Vector(
        "v 1.0 1.0",
        "vn 4.0 1.0 0.0 6.4",
        "vt 4.0 4.0 0.0 4.3",
        "f 1 2 2 1",
        "s on",
        "s 3 2"
      )
      an[IllegalArgumentException] should be thrownBy Wavefront.from(input)
    }

    "transform lines from an .obj file to a wavefront data structure - vertices and faces" in {
      val input = Vector(
        "# a comment",
        "v 1.0 1.0 0.0",
        "v 4.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0",
        "f 1 2 3",
        "f 1 3 4"
      )

      val actual = Wavefront.from(input)
      actual shouldBe Wavefront(
        vertices = Vector(
          new Vector3f(1.0f, 1.0f, 0.0f),
          new Vector3f(4.0f, 1.0f, 0.0f),
          new Vector3f(4.0f, 4.0f, 0.0f),
          new Vector3f(1.0f, 4.0f, 0.0f)
        ),
        triangles = Vector(
          Triangle(
            Indices(1, None, None, None, None),
            Indices(2, None, None, None, None),
            Indices(3, None, None, None, None)
          ),
          Triangle(
            Indices(1, None, None, None, None),
            Indices(3, None, None, None, None),
            Indices(4, None, None, None, None)
          )
        ),
        normals    = Vector.empty[Vector3f],
        textures   = Vector.empty[Vector2f],
        tangents   = Vector.empty[Vector3f],
        biTangents = Vector.empty[Vector3f]
      )
    }

    "transform lines from an .obj file to a wavefront data structure - vertices, textures and faces" in {
      val input = Vector(
        "# a comment",
        "v 1.0 1.0 0.0",
        "v 4.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0",
        "vt 0.0 0.0",
        "vt 1.0 0.0",
        "vt 1.0 1.0",
        "vt 0.0 1.0",
        "f 1/1 2/2 3/3",
        "f 1/1 3/3 4/4"
      )

      val actual = Wavefront.from(input)
      actual shouldBe Wavefront(
        vertices = Vector(
          new Vector3f(1.0f, 1.0f, 0.0f),
          new Vector3f(4.0f, 1.0f, 0.0f),
          new Vector3f(4.0f, 4.0f, 0.0f),
          new Vector3f(1.0f, 4.0f, 0.0f)
        ),
        triangles = Vector(
          Triangle(
            Indices(1, Some(1), None, None, None),
            Indices(2, Some(2), None, None, None),
            Indices(3, Some(3), None, None, None)
          ),
          Triangle(
            Indices(1, Some(1), None, None, None),
            Indices(3, Some(3), None, None, None),
            Indices(4, Some(4), None, None, None)
          )
        ),
        normals = Vector.empty[Vector3f],
        textures = Vector(
          new Vector2f(0.0f, 0.0f),
          new Vector2f(1.0f, 0.0f),
          new Vector2f(1.0f, 1.0f),
          new Vector2f(0.0f, 1.0f)
        ),
        tangents   = Vector.empty[Vector3f],
        biTangents = Vector.empty[Vector3f]
      )
    }

    "transform lines from an .obj file to a wavefront data structure - vertices, normals and faces (smoothShading = true)" in {
      val input = Vector(
        "# a comment",
        "v 1.0 1.0 0.0",
        "v 4.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0",
        "vn 0.0 0.0 1.0",
        "f 1//1 2//1 3//1",
        "f 1//1 3//1 4//1"
      )

      val actual = Wavefront.from(input)
      actual shouldBe Wavefront(
        vertices = Vector(
          new Vector3f(1.0f, 1.0f, 0.0f),
          new Vector3f(4.0f, 1.0f, 0.0f),
          new Vector3f(4.0f, 4.0f, 0.0f),
          new Vector3f(1.0f, 4.0f, 0.0f)
        ),
        triangles = Vector(
          Triangle(
            Indices(1, None, Some(1), None, None),
            Indices(2, None, Some(1), None, None),
            Indices(3, None, Some(1), None, None)
          ),
          Triangle(
            Indices(1, None, Some(1), None, None),
            Indices(3, None, Some(1), None, None),
            Indices(4, None, Some(1), None, None)
          )
        ),
        normals = Vector(
          new Vector3f(0.0f, 0.0f, 1.0f)
        ),
        textures   = Vector.empty[Vector2f],
        tangents   = Vector.empty[Vector3f],
        biTangents = Vector.empty[Vector3f]
      )
    }

    "transform lines from an .obj file to a wavefront data structure - vertices, textures, normals and faces" in {
      val input = Vector(
        "# a comment",
        "v 1.0 1.0 0.0",
        "v 4.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0",
        "vt 0.0 0.0",
        "vt 1.0 0.0",
        "vt 1.0 1.0",
        "vt 0.0 1.0",
        "vn 0.0 0.0 1.0",
        "f 1/1/1 2/2/1 3/3/1",
        "f 1/1/1 3/3/1 4/4/1"
      )

      val actual = Wavefront.from(input)
      actual shouldBe Wavefront(
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
    }
  }
}
