package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class WavefrontTest extends WordSpec with Matchers {
  "A Wavefront" should {

    "x" in {
      val input = Vector(
        "v 16554.0 1.0 1.0",
        "v 1.0 1.0 0.0",
        "v 4.0 4.0 0.0",
        "v 1.0 4.0 0.0 2.3",
        "vt 1.0 1.0",
        "vt 1.2",
        "vn 1.0 1.0 1.0",
        "vn 2.3 3.4",
        "v 1.2.3 3.4 5.6",
        "vt .4 .5 .6",
        "s x",
        "f m 2 2",
        "f 1 3 4"
      )

      val actual = Wavefront.from(input)

      println(actual.get)
      actual shouldBe ""
    }
  }
}
