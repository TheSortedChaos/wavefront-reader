package org.sorted.chaos.wavefront.mesh

import org.scalatest.{Matchers, WordSpec}
import org.sorted.chaos.wavefront.reader.{Indices, Point, Triangle, UVCoordinate, Wavefront}

class WavefrontIndexMeshTest extends WordSpec with Matchers {
  "A WavefrontIndexMesh" should {
    "be created from a wavefront (with vertices, texture and normals definition)" in {
      val vertices = Vector(
        Point(1.0f, 1.0f, 0.0f),
        Point(4.0f, 1.0f, 0.0f),
        Point(4.0f, 4.0f, 0.0f),
        Point(1.0f, 4.0f, 0.0f)
      )
      val textures = Vector(
        UVCoordinate(0.0f, 0.0f),
        UVCoordinate(1.0f, 0.0f),
        UVCoordinate(1.0f, 1.0f),
        UVCoordinate(0.0f, 1.0f)
      )
      val normals = Vector(
        Point(0.1f, 0.1f, 0.0f),
        Point(0.4f, 0.1f, 0.0f),
        Point(0.4f, 0.4f, 0.0f),
        Point(0.1f, 0.4f, 0.0f)
      )
      val triangles = Vector(
        Triangle(
          Indices(1, Some(1), Some(1)),
          Indices(2, Some(2), Some(2)),
          Indices(3, Some(3), Some(3))
        ),
        Triangle(
          Indices(1, Some(1), Some(1)),
          Indices(3, Some(3), Some(3)),
          Indices(4, Some(4), Some(4))
        )
      )

      val input = Wavefront(
        vertices = vertices,
        triangles = triangles,
        normals = normals,
        textures = textures
      )

      val actual = WavefrontIndexMesh.from(input)
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 1.0f, 0.0f, 4.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f,
        4.0f, 0.0f)
      actual.textures should contain theSameElementsInOrderAs Array(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f)
      actual.normals should contain theSameElementsInOrderAs Array(0.1f, 0.1f, 0.0f, 0.4f, 0.1f, 0.0f, 0.4f, 0.4f, 0.0f, 0.1f,
        0.4f, 0.0f)
      actual.indexes should contain theSameElementsInOrderAs Array(0, 1, 2, 0, 2, 3)
    }

    "be created from a wavefront (with vertices and texture)" in {
      val vertices = Vector(
        Point(1.0f, 1.0f, 0.0f),
        Point(4.0f, 1.0f, 0.0f),
        Point(4.0f, 4.0f, 0.0f),
        Point(1.0f, 4.0f, 0.0f)
      )
      val textures = Vector(
        UVCoordinate(0.0f, 0.0f),
        UVCoordinate(1.0f, 0.0f),
        UVCoordinate(1.0f, 1.0f),
        UVCoordinate(0.0f, 1.0f)
      )
      val triangles = Vector(
        Triangle(
          Indices(1, Some(1), None),
          Indices(2, Some(2), None),
          Indices(3, Some(3), None)
        ),
        Triangle(
          Indices(1, Some(1), None),
          Indices(3, Some(3), None),
          Indices(4, Some(4), None)
        )
      )

      val input = Wavefront(
        vertices = vertices,
        triangles = triangles,
        normals = Vector.empty[Point],
        textures = textures
      )
      val actual = WavefrontIndexMesh.from(input)
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 1.0f, 0.0f, 4.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f,
        4.0f, 0.0f)
      actual.textures should contain theSameElementsInOrderAs Array(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f)
      actual.normals shouldBe Array.emptyFloatArray
      actual.indexes should contain theSameElementsInOrderAs Array(0, 1, 2, 0, 2, 3)
    }
  }

}
