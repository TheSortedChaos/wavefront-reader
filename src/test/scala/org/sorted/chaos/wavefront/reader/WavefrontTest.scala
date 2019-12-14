package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, TryValues, WordSpec }

class WavefrontTest extends WordSpec with Matchers with TryValues {
  "A Wavefront" should {

    "handle a broken .obj file" in {}

    "transform lines from an .obj file to a wavefront data structure - vertices and faces" in {
      val input = Vector(
        "v 1.0 1.0 0.0",
        "v 4.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0",
        "s off",
        "f 1 2 3",
        "f 1 3 4"
      )

      val actual = Wavefront.from(input)
      actual shouldBe Wavefront(
        vertices = Vector(
          Point(1.0f, 1.0f, 0.0f),
          Point(4.0f, 1.0f, 0.0f),
          Point(4.0f, 4.0f, 0.0f),
          Point(1.0f, 4.0f, 0.0f)
        ),
        triangles = Vector(
          Triangle(
            Indices(1, None, None),
            Indices(2, None, None),
            Indices(3, None, None)
          ),
          Triangle(
            Indices(1, None, None),
            Indices(3, None, None),
            Indices(4, None, None)
          )
        ),
        normals  = Vector.empty[Point],
        textures = Vector.empty[UVCoordinate]
      )
    }

    "transform lines from an .obj file to a wavefront data structure - vertices, textures and faces" in {
      val input = Vector(
        "v 1.0 1.0 0.0",
        "v 4.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0",
        "vt 0.0 0.0",
        "vt 1.0 0.0",
        "vt 1.0 1.0",
        "vt 0.0 1.0",
        "s off",
        "f 1/1 2/2 3/3",
        "f 1/1 3/3 4/4"
      )

      val actual = Wavefront.from(input)
      actual shouldBe Wavefront(
        vertices = Vector(
          Point(1.0f, 1.0f, 0.0f),
          Point(4.0f, 1.0f, 0.0f),
          Point(4.0f, 4.0f, 0.0f),
          Point(1.0f, 4.0f, 0.0f)
        ),
        triangles = Vector(
          Triangle(
            Indices(1, Some(1), None),
            Indices(2, Some(2), None),
            Indices(3, Some(3), None)
          ),
          Triangle(
            Indices(1, Some(1), None),
            Indices(3, Some(3), None),
            Indices(4, Some(4), None)
          )
        ),
        normals = Vector.empty[Point],
        textures = Vector(
          UVCoordinate(0.0f, 0.0f),
          UVCoordinate(1.0f, 0.0f),
          UVCoordinate(1.0f, 1.0f),
          UVCoordinate(0.0f, 1.0f)
        )
      )
    }

    "transform lines from an .obj file to a wavefront data structure - vertices, normals and faces (smoothShading = true)" in {
      val input = Vector(
        "v 1.0 1.0 0.0",
        "v 4.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0",
        "vn 0.0 0.0 1.0",
        "s 1",
        "f 1//1 2//1 3//1",
        "f 1//1 3//1 4//1"
      )

      val actual = Wavefront.from(input)
      actual shouldBe Wavefront(
        vertices = Vector(
          Point(1.0f, 1.0f, 0.0f),
          Point(4.0f, 1.0f, 0.0f),
          Point(4.0f, 4.0f, 0.0f),
          Point(1.0f, 4.0f, 0.0f)
        ),
        triangles = Vector(
          Triangle(
            Indices(1, None, Some(1)),
            Indices(2, None, Some(1)),
            Indices(3, None, Some(1))
          ),
          Triangle(
            Indices(1, None, Some(1)),
            Indices(3, None, Some(1)),
            Indices(4, None, Some(1))
          )
        ),
        normals = Vector(
          Point(0.0f, 0.0f, 1.0f)
        ),
        textures = Vector.empty[UVCoordinate]
      )
    }

    "transform lines from an .obj file to a wavefront data structure - vertices, textures, normals and faces" in {
      val input = Vector(
        "v 1.0 1.0 0.0",
        "v 4.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0",
        "vt 0.0 0.0",
        "vt 1.0 0.0",
        "vt 1.0 1.0",
        "vt 0.0 1.0",
        "vn 0.0 0.0 1.0",
        "s off",
        "f 1/1/1 2/2/1 3/3/1",
        "f 1/1/1 3/3/1 4/4/1"
      )

      val actual = Wavefront.from(input)
      actual shouldBe Wavefront(
        vertices = Vector(
          Point(1.0f, 1.0f, 0.0f),
          Point(4.0f, 1.0f, 0.0f),
          Point(4.0f, 4.0f, 0.0f),
          Point(1.0f, 4.0f, 0.0f)
        ),
        triangles = Vector(
          Triangle(
            Indices(1, Some(1), Some(1)),
            Indices(2, Some(2), Some(1)),
            Indices(3, Some(3), Some(1))
          ),
          Triangle(
            Indices(1, Some(1), Some(1)),
            Indices(3, Some(3), Some(1)),
            Indices(4, Some(4), Some(1))
          )
        ),
        normals = Vector(
          Point(0.0f, 0.0f, 1.0f)
        ),
        textures = Vector(
          UVCoordinate(0.0f, 0.0f),
          UVCoordinate(1.0f, 0.0f),
          UVCoordinate(1.0f, 1.0f),
          UVCoordinate(0.0f, 1.0f)
        )
      )
    }
  }
}
