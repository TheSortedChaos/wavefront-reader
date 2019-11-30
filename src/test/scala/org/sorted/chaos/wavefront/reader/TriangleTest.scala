package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class TriangleTest extends WordSpec with Matchers {

  "A Triangle" should {

    "be created from a valid .obj file line, like: 'f 1 2 3' (only vertices)" in {
      val input  = "f 1 2 3"
      val actual = Triangle.from(input)
      actual shouldBe Right(
        Triangle(
          Indices(1, None, None),
          Indices(2, None, None),
          Indices(3, None, None)
        )
      )
    }

    "be created from a valid .obj file line like: 'f 1//1 2//2 3//3' (vertices and normals)" in {
      val input  = "f 1//1 2//2 3//3"
      val actual = Triangle.from(input)
      actual shouldBe Right(
        Triangle(
          Indices(1, None, Some(1)),
          Indices(2, None, Some(2)),
          Indices(3, None, Some(3))
        )
      )
    }

    "be created from a valid .obj file line like: 'f 1/1 2/2 3/3' (vertices and textures)" in {
      val input  = "f 1/1 2/2 3/3"
      val actual = Triangle.from(input)
      actual shouldBe Right(
        Triangle(
          Indices(1, Some(1), None),
          Indices(2, Some(2), None),
          Indices(3, Some(3), None)
        )
      )
    }

    "be created from a valid .obj file line like: 'f 1/1/1 2/2/2 3/3/3' (vertices, textures and normals)" in {
      val input  = "f 1/1/1 2/2/2 3/3/3"
      val actual = Triangle.from(input)
      actual shouldBe Right(
        Triangle(
          Indices(1, Some(1), Some(1)),
          Indices(2, Some(2), Some(2)),
          Indices(3, Some(3), Some(3))
        )
      )
    }

    "create an error message, if the line does not exactly contain 4 arguments" in {
      val input  = "f 1 2"
      val actual = Triangle.from(input)
      actual shouldBe Left(
        "  * ParseError for Triangle definition - pattern consists of 4 arguments [token indices indices indices], " +
        "but 3 argument(s) was/were found (source was: 'f 1 2')."
      )
    }

    "create an error message, if a transformation pattern could not found (no number)" in {
      val input  = "f 1 2 a"
      val actual = Triangle.from(input)
      actual shouldBe Left("  * ParseError for Triangle definition - no pattern found for 'f 1 2 a'")
    }

    "create an error message, if a transformation pattern could not found (no int)" in {
      val input  = "f 1 2 1.2"
      val actual = Triangle.from(input)
      actual shouldBe Left("  * ParseError for Triangle definition - no pattern found for 'f 1 2 1.2'")
    }
  }
}
