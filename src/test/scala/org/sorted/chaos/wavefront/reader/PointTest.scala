package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class PointTest extends WordSpec with Matchers {

  "A Point" should {

    "be created from a valid .obj file line, like 'v 1.0 1.0 1.0'" in {
      val input  = "v 1.0 1.0 1.0"
      val actual = Point.from(input)
      actual shouldBe Right(
        Point(1.0f, 1.0f, 1.0f)
      )
    }

    "create an error message, if the line does not contain exactly 4 parts [token number number number]" in {
      val input  = "v 1.0 1.0 1.0 1.0"
      val actual = Point.from(input)
      actual shouldBe Left(
        "  * There are 4 arguments [token number number number] needed to parse a Vertex/Normal coordinate, " +
        "but 5 argument(s) was/were found (source was: 'v 1.0 1.0 1.0 1.0')."
      )
    }

    "create an error message, if the numbers could not parsed to Floats" in {
      val input  = "v 1.0 1.a 1.0"
      val actual = Point.from(input)
      actual shouldBe Left(
        "  * There are 3 numbers needed to parse a Vertex/Normal coordinate, but something could not transformed " +
        "to a Float (source was: 'v 1.0 1.a 1.0')."
      )
    }
  }
}
