package org.sorted.chaos.wavefront.reader

import org.scalatest.{ Matchers, WordSpec }

class MaterialTest extends WordSpec with Matchers {

  "A Material" should {
    "be created from valid String lines" in {
      val input = Vector(
        "# here is a comment",
        "Ka 0.1 0.2 0.3",
        "Kd 0.4 0.5 0.6",
        "Ks 0.7 0.8 0.9",
        "Ns 10.5",
        "d 0.75"
      )
      val actual = Material.from(input)

      actual shouldBe Material(
        ambientColor     = Color(0.1f, 0.2f, 0.3f),
        diffuseColor     = Color(0.4f, 0.5f, 0.6f),
        specularColor    = Color(0.7f, 0.8f, 0.9f),
        specularExponent = 10.5f,
        alphaChannel     = 0.75f
      )
    }

    "throw an exception if something is invalid" in {
      val input = Vector(
        "Ka 0.1 0.2 0.3 0.123456",
        "Kd 0.4 0.5 0.6",
        "Ks 0.7 0.8 0.9",
        "Ns 10.5",
        "d 0.75"
      )
      an[IllegalArgumentException] should be thrownBy Material.from(input)
    }
  }
}
