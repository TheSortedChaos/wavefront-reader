package org.sorted.chaos.wavefront

import org.joml.Vector3f
import org.scalatest.{Matchers, WordSpec}
import org.sorted.chaos.wavefront.mesh.Mesh
import org.sorted.chaos.wavefront.reader.Material

class WavefrontReaderTest extends WordSpec with Matchers {

  "The WavefrontReader" should {
    "read an .obj file with vertices" in {
      val actual = WavefrontReader.from("/plane-with-vertices.obj")
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f)
      actual.textures shouldBe Array.emptyFloatArray
      actual.normals shouldBe Array.emptyFloatArray
    }
    "read an .obj file with vertices and textures" in {
      val actual = WavefrontReader.from("/plane-with-vertices-textures.obj")
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f)
      actual.textures should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f)
      actual.normals shouldBe Array.emptyFloatArray
    }
    "read an .obj file with vertices, textures and normals" in {
      val actual = WavefrontReader.from("/plane-with-vertices-textures-normals.obj")
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f)
      actual.textures should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f)
      actual.normals should contain theSameElementsInOrderAs Array(0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f)

      val normalMapping = Mesh.normalMapping(actual)
      println("aaaahhh")
    }
    "read an .obj file with vertices (create index list for index drawing)" in {
      val actual = WavefrontReader.withIndexFrom("/plane-with-vertices.obj")
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, -1.0f)
      actual.textures shouldBe Array.emptyFloatArray
      actual.normals shouldBe Array.emptyFloatArray
      actual.indexes should contain theSameElementsInOrderAs Array(0, 1, 2, 0, 3, 1)
    }
    "read an .obj file with vertices and textures (create index list for index drawing)" in {
      val actual = WavefrontReader.withIndexFrom("/plane-with-vertices-textures.obj")
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, -1.0f)
      actual.textures should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f)
      actual.normals shouldBe Array.emptyFloatArray
      actual.indexes should contain theSameElementsInOrderAs Array(0, 1, 2, 0, 3, 1)
    }
    "read an .obj file with vertices, textures and normals (create index list for index drawing)" in {
      val actual = WavefrontReader.withIndexFrom("/plane-with-vertices-textures-normals.obj")
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, -1.0f)
      actual.textures should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f)
      actual.normals should contain theSameElementsInOrderAs Array(0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        1.0f, 0.0f)
      actual.indexes should contain theSameElementsInOrderAs Array(0, 1, 2, 0, 3, 1)
    }
    "read an .obj file with vertices (for SimpleMesh)" in {
      val actual = WavefrontReader.simpleFrom(
        "/plane-with-vertices.obj",
        new Vector3f(0.2f, 0.4f, 0.6f)
      )
      actual.vertices should contain theSameElementsInOrderAs Array(1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f)
      actual.color should contain theSameElementsInOrderAs Array(0.2f, 0.4f, 0.6f, 0.2f, 0.4f, 0.6f, 0.2f, 0.4f, 0.6f, 0.2f, 0.4f,
        0.6f, 0.2f, 0.4f, 0.6f, 0.2f, 0.4f, 0.6f)
    }
    "read an .obj file with vertices (for SimpleMesh and create index list for index drawing)" in {
      val actual = WavefrontReader.simpleWithIndexFrom(
        "/plane-with-vertices.obj",
        new Vector3f(0.2f, 0.4f, 0.6f)
      )
      actual.vertices should contain theSameElementsInOrderAs Array(-1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f,
        0.0f, -1.0f)
      actual.color should contain theSameElementsInOrderAs Array(0.2f, 0.4f, 0.6f, 0.2f, 0.4f, 0.6f, 0.2f, 0.4f, 0.6f, 0.2f, 0.4f,
        0.6f)
      actual.indexes should contain theSameElementsInOrderAs Array(1, 2, 0, 1, 3, 2)
    }
    "read an .mtl file" in {
      val actual = WavefrontReader.materialFrom("/material.mtl")
      actual shouldBe Material(
        ambientColor     = new Vector3f(0.1f, 0.2f, 0.3f),
        diffuseColor     = new Vector3f(0.6f, 0.5f, 0.4f),
        specularColor    = new Vector3f(0.7f, 0.8f, 0.9f),
        specularExponent = 500.0f
      )
    }
  }
}
