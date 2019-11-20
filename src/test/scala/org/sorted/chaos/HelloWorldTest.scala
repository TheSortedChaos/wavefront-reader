package org.sorted.chaos

import org.scalatest.{ Matchers, WordSpec }

class HelloWorldTest extends WordSpec with Matchers {
  "Initial Test" should {
    "be true" in {
      5 shouldBe 5
    }
  }
}
