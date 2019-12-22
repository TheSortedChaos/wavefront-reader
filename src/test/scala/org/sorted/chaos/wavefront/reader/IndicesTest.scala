package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class IndicesTest extends WordSpec with Matchers {
  import Indices._

  "Indices" should {
    "extracted from a valid vertex-definition" in {
      val input = "1"
      input.getIndices shouldBe Some(Indices(1, None, None))
    }

    "extracted from a valid vertex-texture-definition" in {
      val input = "1/2"
      input.getIndices shouldBe Some(Indices(1, Some(2), None))
    }

    "extracted from a valid vertex-normal-definition" in {
      val input = "1//3"
      input.getIndices shouldBe Some(Indices(1, None, Some(3)))
    }

    "extracted from a valid vertex-texture-normal-definition" in {
      val input = "1/2/3"
      input.getIndices shouldBe Some(Indices(1, Some(2), Some(3)))
    }

    "fail" in {
      val input = "1/2/3/5"
      input.getIndices shouldBe None
    }
  }
}
