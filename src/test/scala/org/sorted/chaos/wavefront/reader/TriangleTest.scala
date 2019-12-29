package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class TriangleTest extends WordSpec with Matchers {
  import Triangle._

  "A Triangle" should {
    "be extracted from a valid String -> 'f 1 2 3' (only vertices)" in {
      val input = "f 1 2 3"
      input.getTriangle shouldBe Triangle(
        Indices(1, None, None, None, None),
        Indices(2, None, None, None, None),
        Indices(3, None, None, None, None)
      )
    }

    "be extracted from a valid String -> 'f 1//1 2//2 3//3' (vertices and normals)" in {
      val input = "f 1//1 2//2 3//3"
      input.getTriangle shouldBe Triangle(
        Indices(1, None, Some(1), None, None),
        Indices(2, None, Some(2), None, None),
        Indices(3, None, Some(3), None, None)
      )
    }

    "be extracted from a valid String -> 'f 1/1 2/2 3/3' (vertices and textures)" in {
      val input = "f 1/1 2/2 3/3"
      input.getTriangle shouldBe Triangle(
        Indices(1, Some(1), None, None, None),
        Indices(2, Some(2), None, None, None),
        Indices(3, Some(3), None, None, None)
      )
    }

    "be extracted from a valid String -> 'f 1/1/1 2/2/2 3/3/3' (vertices, textures and normals)" in {
      val input = "f 1/1/1 2/2/2 3/3/3"
      input.getTriangle shouldBe Triangle(
        Indices(1, Some(1), Some(1), None, None),
        Indices(2, Some(2), Some(2), None, None),
        Indices(3, Some(3), Some(3), None, None)
      )
    }

    "NOT be extracted from an invalid String - (one index missing)" in {
      val input = "TOKEN 1/1/1 2/2/2"
      the[IllegalArgumentException] thrownBy input.getTriangle should have message "requirement failed: Reading a 'Triangle' needs 4 parts [token indices indices indices]. Found 3 part(s) in line 'TOKEN 1/1/1 2/2/2'."
    }

    "NOT be extracted from an invalid String - (one index too much)" in {
      val input = "TOKEN 1/1/1 2/2/2 3/3/3 4/4/4"
      the[IllegalArgumentException] thrownBy input.getTriangle should have message "requirement failed: Reading a 'Triangle' needs 4 parts [token indices indices indices]. Found 5 part(s) in line 'TOKEN 1/1/1 2/2/2 3/3/3 4/4/4'."
    }

    "NOT be extracted from an invalid String - (invalid index)" in {
      val input = "TOKEN 1/1/1 2/2/2 ups"
      the[IllegalArgumentException] thrownBy input.getTriangle should have message "requirement failed: Creating a 'Triangle' needs 3 Indices. Found 2 indices in line 'TOKEN 1/1/1 2/2/2 ups'."
    }

    "add normal mapping indexes" in {
      val triangle = Triangle(
        Indices(
          vertexIndex     = 1,
          textureIndex    = Some(1),
          normalIndex     = Some(1),
          tangentsIndex   = None,
          biTangentsIndex = None
        ),
        Indices(
          vertexIndex     = 2,
          textureIndex    = Some(2),
          normalIndex     = Some(2),
          tangentsIndex   = None,
          biTangentsIndex = None
        ),
        Indices(
          vertexIndex     = 3,
          textureIndex    = Some(3),
          normalIndex     = Some(3),
          tangentsIndex   = None,
          biTangentsIndex = None
        )
      )

      val actual = triangle.addNormalMappingIndex(5, 8)
      actual shouldBe Triangle(
        Indices(
          vertexIndex     = 1,
          textureIndex    = Some(1),
          normalIndex     = Some(1),
          tangentsIndex   = Some(5),
          biTangentsIndex = Some(8)
        ),
        Indices(
          vertexIndex     = 2,
          textureIndex    = Some(2),
          normalIndex     = Some(2),
          tangentsIndex   = Some(5),
          biTangentsIndex = Some(8)
        ),
        Indices(
          vertexIndex     = 3,
          textureIndex    = Some(3),
          normalIndex     = Some(3),
          tangentsIndex   = Some(5),
          biTangentsIndex = Some(8)
        )
      )
    }
  }
}
