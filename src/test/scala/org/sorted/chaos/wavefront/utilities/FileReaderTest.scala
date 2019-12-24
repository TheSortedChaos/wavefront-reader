package org.sorted.chaos.wavefront.utilities

import org.scalatest.{Matchers, WordSpec}

class FileReaderTest extends WordSpec with Matchers {
  "A FileReader" should {
    "read a text file" in {
      val actual = FileReader.read("/text.txt")
      actual shouldBe Vector("1. line", "2. line", "end of file")
    }
    "return an empty Vector, if the file does not exist" in {
      val actual = FileReader.read("file-does-not-exist.txt")
      actual shouldBe Vector.empty[String]
    }
  }
}
