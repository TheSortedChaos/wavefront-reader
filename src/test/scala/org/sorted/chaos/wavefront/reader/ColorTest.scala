package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class ColorTest extends WordSpec with Matchers {
  import Color._

  "A Color" should {
    "extracted from a valid String" in {
      val input = "Token 0.2 0.3 0.4"
      input.getColor shouldBe Color(0.2f, 0.3f, 0.4f)
    }

    "NOT extracted from an invalid String - (one number missing)" in {
      val input = "Token 0.2 0.3"
      the[IllegalArgumentException] thrownBy input.getColor should have message "requirement failed: Reading a 'Color' needs 4 parts [token red green blue]. Found 3 part(s) in line 'Token 0.2 0.3'."
    }

    "NOT extracted from an invalid String - (one number too much)" in {
      val input = "Token 0.2 0.3 0.4 0.5"
      the[IllegalArgumentException] thrownBy input.getColor should have message "requirement failed: Reading a 'Color' needs 4 parts [token red green blue]. Found 5 part(s) in line 'Token 0.2 0.3 0.4 0.5'."
    }

    "NOT extracted from an invalid String - (no Float number)" in {
      val input = "Token 0.2 0.3 ups"
      the[IllegalArgumentException] thrownBy input.getColor should have message "requirement failed: Reading a 'Color' needs 3 Float numbers. Found 2 Float number(s) in line 'Token 0.2 0.3 ups'."
    }
  }
}
