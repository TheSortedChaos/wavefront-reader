package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class ExtractSingleFloatTest extends WordSpec with Matchers {
  import ExtractSingleFloat._

  "An ExtractSingleFloat" should {
    "extract the 'Specular Exponent' from a valid String" in {
      val input = "TOKEN 12.3"
      input.getSpecularExponent shouldBe 12.3f
    }

    "NOT extract the 'Specular Exponent' from an invalid String - (number missing)" in {
      val input = "TOKEN"
      the[IllegalArgumentException] thrownBy input.getSpecularExponent should have message "requirement failed: Reading a 'Specular Exponent' needs 2 parts [token value]. Found 1 part(s) in line 'TOKEN'."
    }

    "NOT extract the 'Specular Exponent' from an invalid String - (one number too much)" in {
      val input = "TOKEN 1.2 3.4"
      the[IllegalArgumentException] thrownBy input.getSpecularExponent should have message "requirement failed: Reading a 'Specular Exponent' needs 2 parts [token value]. Found 3 part(s) in line 'TOKEN 1.2 3.4'."
    }

    "NOT extract the 'Specular Exponent' from an invalid String - (no Float number)" in {
      val input = "TOKEN ab.c"
      the[IllegalArgumentException] thrownBy input.getSpecularExponent should have message "requirement failed: Reading a 'Specular Exponent' needs 1 Float number. Found 0 Float number(s) in line 'TOKEN ab.c'."
    }

    "extract the 'Alpha Channel' from a valid String" in {
      val input = "TOKEN 0.5"
      input.getAlphaChannel shouldBe 0.5
    }

    "NOT extract the 'Alpha Channel' from an invalid String - (number missing)" in {
      val input = "TOKEN"
      the[IllegalArgumentException] thrownBy input.getAlphaChannel should have message "requirement failed: Reading a 'Alpha Channel' needs 2 parts [token value]. Found 1 part(s) in line 'TOKEN'."
    }

    "NOT extract the 'Alpha Channel' from an invalid String - (one number too much)" in {
      val input = "TOKEN 1.2 3.4"
      the[IllegalArgumentException] thrownBy input.getAlphaChannel should have message "requirement failed: Reading a 'Alpha Channel' needs 2 parts [token value]. Found 3 part(s) in line 'TOKEN 1.2 3.4'."
    }

    "NOT extract the 'Alpha Channel' from an invalid String - (no Float number)" in {
      val input = "TOKEN ab.c"
      the[IllegalArgumentException] thrownBy input.getAlphaChannel should have message "requirement failed: Reading a 'Alpha Channel' needs 1 Float number. Found 0 Float number(s) in line 'TOKEN ab.c'."
    }
  }
}
