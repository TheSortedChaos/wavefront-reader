package org.sorted.chaos.wavefront.mesh

import org.scalatest.{ Matchers, WordSpec }
import org.sorted.chaos.wavefront.reader.{ Indices, Point, Triangle, UVCoordinate, Wavefront }

class TexturedIndexedMeshTest extends WordSpec with Matchers {
  "A TexturedIndexedMesh" should {
    "be created from a wavefront (with vertices and texture definition)" in {
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
        vertices      = vertices,
        triangles     = triangles,
        normals       = Vector.empty[Point],
        textures      = textures,
        smoothShading = false
      )

      val actual = TexturedIndexedMesh.from(input)
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 1.0f, 0.0f, 4.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 1.0f,
        4.0f, 0.0f)
      actual.textures should contain theSameElementsInOrderAs Array(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f)
      actual.indexes should contain theSameElementsInOrderAs Array(0, 1, 2, 0, 2, 3)

    }
  }
}
