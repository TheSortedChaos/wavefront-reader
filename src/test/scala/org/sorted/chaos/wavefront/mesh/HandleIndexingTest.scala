package org.sorted.chaos.wavefront.mesh

import org.scalatest.{Matchers, WordSpec}
import org.sorted.chaos.wavefront.reader.{Indices, Point, Triangle, UVCoordinate, Wavefront}
import org.sorted.chaos.wavefront.utilities.FileReader

class HandleIndexingTest extends WordSpec with Matchers {
  "Test Doit" should {
    "do something" in {
      val actual = HandleIndexing.doit(input2)
      println(actual)
      val actual2 = HandleIndexing.doit2(input2)
      println(actual2)
      
      actual shouldBe actual2
    }
  }
  
  
  private def input2 = {
   val lines = FileReader.read("textured-cube.obj")
    Wavefront.from(lines).get
  }
  
  private def input = {
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

    Wavefront(
      vertices      = vertices,
      triangles     = triangles,
      normals       = Vector.empty[Point],
      textures      = textures,
      smoothShading = false
    )
  }
}
