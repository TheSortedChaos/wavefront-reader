package org.sorted.chaos

import org.scalatest.{Matchers, WordSpec}

class SimpleFunctionTest extends WordSpec with Matchers {
  "SimpleFunction" should {
    "add two numbers" in {
      SimpleFunction.add(2, 3) shouldBe 5
    }
  }
}
