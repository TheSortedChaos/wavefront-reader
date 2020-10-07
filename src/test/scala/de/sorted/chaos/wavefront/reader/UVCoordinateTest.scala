package de.sorted.chaos.wavefront.reader

import org.joml.Vector2f
import org.scalatest.{ Matchers, WordSpec }

class UVCoordinateTest extends WordSpec with Matchers {
  import UVCoordinate._

  "A UVCoordinate" should {
    "be extracted from a valid String" in {
      val input = "TOKEN 1.0 2.0"
      input.getUVCoordinate shouldBe new Vector2f(1.0f, 2.0f)
    }

    "NOT be extracted from an invalid String - (one number missing)" in {
      val input = "TOKEN 1.0"
      the[IllegalArgumentException] thrownBy input.getUVCoordinate should have message "requirement failed: Reading a 'Texture' needs 3 parts [token u v]. Found 2 part(s) in line 'TOKEN 1.0'."
    }

    "NOT be extracted from an invalid String - (one number too much)" in {
      val input = "TOKEN 1.0 2.0 3.0"
      the[IllegalArgumentException] thrownBy input.getUVCoordinate should have message "requirement failed: Reading a 'Texture' needs 3 parts [token u v]. Found 4 part(s) in line 'TOKEN 1.0 2.0 3.0'."
    }

    "NOT be extracted from an invalid String - (no Float number)" in {
      val input = "TOKEN 1.0 ups"
      the[IllegalArgumentException] thrownBy input.getUVCoordinate should have message "requirement failed: Reading a 'Texture' needs 2 Float numbers. Found 1 Float number(s) in line 'TOKEN 1.0 ups'."
    }
  }
}
