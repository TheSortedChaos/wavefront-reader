package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class PointTest extends WordSpec with Matchers {
    import Point._

  "A Point" should {
    "be extracted from a valid String" in {
      val input  = "TOKEN 1.0 2.0 3.0"
      input.getPoint shouldBe Point(1.0f, 2.0f, 3.0f)
    }

    "NOT be extracted from an invalid String - (one number missing)" in {
      val input = "TOKEN 1.0 3.0"
      the[IllegalArgumentException] thrownBy input.getPoint should have message "requirement failed: Reading a 'Vertex' or 'Normal' needs 4 parts [token x y z]. Found 3 part(s) in line 'TOKEN 1.0 3.0'."
    }

    "NOT be extracted from an invalid String - (one number too much)" in {
      val input = "TOKEN 1.0 2.0 3.0 4.0"
      the[IllegalArgumentException] thrownBy input.getPoint should have message "requirement failed: Reading a 'Vertex' or 'Normal' needs 4 parts [token x y z]. Found 5 part(s) in line 'TOKEN 1.0 2.0 3.0 4.0'."
    }

    "NOT be extracted from an invalid String - (no Float number)" in {
      val input = "TOKEN 1.0 2.0 ups"
      the[IllegalArgumentException] thrownBy input.getPoint should have message "requirement failed: Reading a 'Vertex' or 'Normal' needs 3 Float numbers. Found 2 Float number(s) in line 'TOKEN 1.0 2.0 ups'."
    }
  }
}
