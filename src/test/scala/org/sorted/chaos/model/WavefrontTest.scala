package org.sorted.chaos.model

import org.scalatest.{ Matchers, WordSpec }

class WavefrontTest extends WordSpec with Matchers {
  "A Wavefront" should {
    "be created from a text file" in {
      val actual = Wavefront.createFrom("simple-cube.obj")
      actual shouldBe Wavefront(
        Vector(
          Point(1.0f, 1.0f, -1.0f),
          Point(1.0f, -1.0f, -1.0f),
          Point(1.0f, 1.0f, 1.0f),
          Point(1.0f, -1.0f, 1.0f),
          Point(-1.0f, 1.0f, -1.0f),
          Point(-1.0f, -1.0f, -1.0f),
          Point(-1.0f, 1.0f, 1.0f),
          Point(-1.0f, -1.0f, 1.0f)
        ),
        Vector(
          Face(4, 2, 0),
          Face(2, 7, 3),
          Face(6, 5, 7),
          Face(1, 7, 5),
          Face(0, 3, 1),
          Face(4, 1, 5),
          Face(4, 6, 2),
          Face(2, 6, 7),
          Face(6, 4, 5),
          Face(1, 3, 7),
          Face(0, 2, 3),
          Face(4, 0, 1)
        )
      )
    }
  }
}
