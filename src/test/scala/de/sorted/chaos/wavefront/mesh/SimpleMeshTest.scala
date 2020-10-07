package de.sorted.chaos.wavefront.mesh

import de.sorted.chaos.wavefront.reader.{Indices, Triangle, Wavefront}
import org.joml.{Vector2f, Vector3f}
import org.scalatest.{Matchers, WordSpec}

class SimpleMeshTest extends WordSpec with Matchers {

  "A SimpleMesh" should {
    "be created from a wavefront and a color definition" in {
      val vertices = Vector(
        new Vector3f(1.0f, 1.0f, 0.0f),
        new Vector3f(4.0f, 1.0f, 0.0f),
        new Vector3f(4.0f, 4.0f, 0.0f),
        new Vector3f(1.0f, 4.0f, 0.0f)
      )
      val triangles = Vector(
        Triangle(
          Indices(1, None, None, None, None),
          Indices(2, None, None, None, None),
          Indices(3, None, None, None, None)
        ),
        Triangle(
          Indices(1, None, None, None, None),
          Indices(3, None, None, None, None),
          Indices(4, None, None, None, None)
        )
      )

      val wavefront = Wavefront(
        vertices   = vertices,
        triangles  = triangles,
        normals    = Vector.empty[Vector3f],
        textures   = Vector.empty[Vector2f],
        tangents   = Vector.empty[Vector3f],
        biTangents = Vector.empty[Vector3f]
      )
      val color = new Vector3f(0.3f, 0.4f, 0.5f)

      val actual = SimpleMesh.from(wavefront, color)
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 1.0f, 0.0f, 4.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f, 4.0f, 0.0f)
      actual.color should contain theSameElementsInOrderAs Array(0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f, 0.3f, 0.4f,
        0.5f, 0.3f, 0.4f, 0.5f, 0.3f, 0.4f, 0.5f)
    }
  }
}
