package org.sorted.chaos.process

import org.scalatest.{ Matchers, WordSpec }
import org.sorted.chaos.model.{ Face, Point, Wavefront }

class SimpleMeshTest extends WordSpec with Matchers {
  "A SimpleMesh" should {
    "be created from a wavefront and a color definition" in {
      val vertices = Vector(
        Point(1.0f, 1.0f, 1.0f),
        Point(2.0f, 2.0f, 2.0f),
        Point(3.0f, 3.0f, 3.0f)
      )
      val faces     = Vector(Face(2, 0, 1))
      val wavefront = Wavefront(vertices, faces)
      val color     = SolidColor(0.3f, 0.4f, 0.5f)

      val actual = SimpleMesh.createFrom(wavefront, color)
      actual.vertices should contain theSameElementsInOrderAs Array(3.0f, 3.0f, 3.0f, 1.0f, 1.0f, 1.0f, 2.0f, 2.0f, 2.0f)
      actual.color should contain theSameElementsInOrderAs Array(0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f)
    }
  }
}
