package org.sorted.chaos.process

import org.scalatest.{ Matchers, WordSpec }
import org.sorted.chaos.model.{ Index, Point, Triangle, UVCoordinate, Wavefront }

class SimpleIndexedMeshTest extends WordSpec with Matchers {
  "A SimpleIndexedMesh" should {
    "be created from a wavefront and a color definition" in {
      val vertices = Vector(
        Point(1.0f, 1.0f, 0.0f),
        Point(4.0f, 1.0f, 0.0f),
        Point(4.0f, 4.0f, 0.0f),
        Point(1.0f, 4.0f, 0.0f)
      )
      val triangles = Vector(
        Triangle(
          Index(1, None, None),
          Index(2, None, None),
          Index(3, None, None)
        ),
        Triangle(
          Index(1, None, None),
          Index(3, None, None),
          Index(4, None, None)
        )
      )

      val wavefront = Wavefront(
        vertices      = vertices,
        triangles     = triangles,
        normals       = Vector.empty[Point],
        textures      = Vector.empty[UVCoordinate],
        smoothShading = false
      )
      val color = SolidColor(0.3f, 0.4f, 0.5f)

      val actual = SimpleIndexedMesh.from(wavefront, color)
      actual.indexes should contain theSameElementsInOrderAs Array(0, 1, 2, 0, 2, 3)
      actual.color should contain theSameElementsInOrderAs Array(0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f, 0.3f, 0.4f,
        0.5f)
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 1.0f, 0.0f, 4.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f,
        4.0f, 0.0f)
    }
  }
}
