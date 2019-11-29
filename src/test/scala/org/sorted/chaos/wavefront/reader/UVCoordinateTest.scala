package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class UVCoordinateTest extends WordSpec with Matchers {

  "A UVCoordinate" should {

    "be created from a valid .obj file line, like 'vt 1.0 1.0'" in {
      val input  = "vt 1.0 1.0"
      val actual = UVCoordinate.from(input)
      actual shouldBe Right(
        UVCoordinate(1.0f, 1.0f)
      )
    }

    "create an error message, if the line does not contain exactly 3 parts [token number number]" in {
      val input  = "vt 1.0 1.0 1.0"
      val actual = UVCoordinate.from(input)
      actual shouldBe Left(
        "  * There are 3 arguments [token number number] needed to parse a Texture coordinate, " +
        "but 4 argument(s) was/were found (source was: 'vt 1.0 1.0 1.0')."
      )
    }

    "create an error message, if the numbers could not parsed to Floats" in {
      val input  = "vt 1.0 1.a"
      val actual = UVCoordinate.from(input)
      actual shouldBe Left(
        "" +
        "  * There are 2 numbers needed to parse a Texture coordinate, but something could not transformed " +
        "to a Float (source was: 'vt 1.0 1.a)'."
      )
    }
  }
}
