package de.sorted.chaos.wavefront.mesh

import de.sorted.chaos.wavefront.reader.{ Indices, Triangle, Wavefront }
import org.joml.{ Vector2f, Vector3f }
import org.scalatest.{ Matchers, WordSpec }

class MeshTest extends WordSpec with Matchers {

  "A Mesh" should {
    "be created from a wavefront (with vertices, texture and normals definition)" in {
      val vertices = Vector(
        new Vector3f(1.0f, 1.0f, 0.0f),
        new Vector3f(4.0f, 1.0f, 0.0f),
        new Vector3f(4.0f, 4.0f, 0.0f),
        new Vector3f(1.0f, 4.0f, 0.0f)
      )
      val textures = Vector(
        new Vector2f(0.0f, 0.0f),
        new Vector2f(1.0f, 0.0f),
        new Vector2f(1.0f, 1.0f),
        new Vector2f(0.0f, 1.0f)
      )
      val normals = Vector(
        new Vector3f(0.1f, 0.1f, 0.0f),
        new Vector3f(0.4f, 0.1f, 0.0f),
        new Vector3f(0.4f, 0.4f, 0.0f),
        new Vector3f(0.1f, 0.4f, 0.0f)
      )
      val triangles = Vector(
        Triangle(
          Indices(1, Some(1), Some(1), None, None),
          Indices(2, Some(2), Some(2), None, None),
          Indices(3, Some(3), Some(3), None, None)
        ),
        Triangle(
          Indices(1, Some(1), Some(1), None, None),
          Indices(3, Some(3), Some(3), None, None),
          Indices(4, Some(4), Some(4), None, None)
        )
      )

      val input = Wavefront(
        vertices   = vertices,
        triangles  = triangles,
        normals    = normals,
        textures   = textures,
        tangents   = Vector.empty[Vector3f],
        biTangents = Vector.empty[Vector3f]
      )

      val actual = Mesh.from(input)
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 1.0f, 0.0f, 4.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f, 4.0f, 0.0f)
      actual.textures should contain theSameElementsInOrderAs Array(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f)
      actual.normals should contain theSameElementsAs Array(0.1f, 0.1f, 0.0f, 0.4f, 0.1f, 0.0f, 0.4f, 0.4f, 0.0f, 0.1f, 0.1f,
        0.0f, 0.4f, 0.4f, 0.0f, 0.1f, 0.4f, 0.0f)
      actual.tangents shouldBe Array.emptyFloatArray
      actual.biTangents shouldBe Array.emptyFloatArray
    }

    "be created from a wavefront (with vertices and texture)" in {
      val vertices = Vector(
        new Vector3f(1.0f, 1.0f, 0.0f),
        new Vector3f(4.0f, 1.0f, 0.0f),
        new Vector3f(4.0f, 4.0f, 0.0f),
        new Vector3f(1.0f, 4.0f, 0.0f)
      )
      val textures = Vector(
        new Vector2f(0.0f, 0.0f),
        new Vector2f(1.0f, 0.0f),
        new Vector2f(1.0f, 1.0f),
        new Vector2f(0.0f, 1.0f)
      )
      val triangles = Vector(
        Triangle(
          Indices(1, Some(1), None, None, None),
          Indices(2, Some(2), None, None, None),
          Indices(3, Some(3), None, None, None)
        ),
        Triangle(
          Indices(1, Some(1), None, None, None),
          Indices(3, Some(3), None, None, None),
          Indices(4, Some(4), None, None, None)
        )
      )

      val input = Wavefront(
        vertices   = vertices,
        triangles  = triangles,
        normals    = Vector.empty[Vector3f],
        textures   = textures,
        tangents   = Vector.empty[Vector3f],
        biTangents = Vector.empty[Vector3f]
      )

      val actual = Mesh.from(input)
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 1.0f, 0.0f, 4.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f, 4.0f, 0.0f)
      actual.textures should contain theSameElementsInOrderAs Array(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f)
      actual.normals shouldBe Array.emptyFloatArray
      actual.tangents shouldBe Array.emptyFloatArray
      actual.biTangents shouldBe Array.emptyFloatArray
    }
  }
}
