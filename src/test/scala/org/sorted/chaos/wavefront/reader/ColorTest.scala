package org.sorted.chaos.wavefront.reader

import org.joml.Vector3f
import org.scalatest.{Matchers, WordSpec}

class ColorTest extends WordSpec with Matchers {
  import Color._

  "A Color" should {
    "be extracted from a valid String" in {
      val input = "TOKEN 0.2 0.3 0.4"
      input.getColor shouldBe new Vector3f(0.2f, 0.3f, 0.4f)
    }

    "NOT be extracted from an invalid String - (one number missing)" in {
      val input = "TOKEN 0.2 0.3"
      the[IllegalArgumentException] thrownBy input.getColor should have message "requirement failed: Reading a 'Color' needs 4 parts [token red green blue]. Found 3 part(s) in line 'TOKEN 0.2 0.3'."
    }

    "NOT be extracted from an invalid String - (one number too much)" in {
      val input = "TOKEN 0.2 0.3 0.4 0.5"
      the[IllegalArgumentException] thrownBy input.getColor should have message "requirement failed: Reading a 'Color' needs 4 parts [token red green blue]. Found 5 part(s) in line 'TOKEN 0.2 0.3 0.4 0.5'."
    }

    "NOT be extracted from an invalid String - (no Float number)" in {
      val input = "TOKEN 0.2 0.3 ups"
      the[IllegalArgumentException] thrownBy input.getColor should have message "requirement failed: Reading a 'Color' needs 3 Float numbers. Found 2 Float number(s) in line 'TOKEN 0.2 0.3 ups'."
    }
  }
}
