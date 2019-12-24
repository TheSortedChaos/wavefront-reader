package org.sorted.chaos.wavefront.mesh

import org.joml.{ Vector2f, Vector3f }
import org.scalatest.{ Matchers, WordSpec }
import org.sorted.chaos.wavefront.reader.{ Indices, Triangle, Wavefront }

class SimpleIndexMeshTest extends WordSpec with Matchers {

  "A SimpleIndexMesh" should {
    "be created from a wavefront and a color definition" in {
      val vertices = Vector(
        new Vector3f(1.0f, 1.0f, 0.0f),
        new Vector3f(4.0f, 1.0f, 0.0f),
        new Vector3f(4.0f, 4.0f, 0.0f),
        new Vector3f(1.0f, 4.0f, 0.0f)
      )
      val triangles = Vector(
        Triangle(
          Indices(1, None, None),
          Indices(2, None, None),
          Indices(3, None, None)
        ),
        Triangle(
          Indices(1, None, None),
          Indices(3, None, None),
          Indices(4, None, None)
        )
      )

      val wavefront = Wavefront(
        vertices  = vertices,
        triangles = triangles,
        normals   = Vector.empty[Vector3f],
        textures  = Vector.empty[Vector2f]
      )
      val color = new Vector3f(0.3f, 0.4f, 0.5f)

      val actual = SimpleIndexMesh.from(wavefront, color)
      actual.indexes should contain theSameElementsInOrderAs Array(0, 1, 2, 0, 2, 3)
      actual.color should contain theSameElementsInOrderAs Array(0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f, 0.3f, 0.4f,
        0.5f)
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 1.0f, 0.0f, 4.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f,
        4.0f, 0.0f)
    }
  }
}
