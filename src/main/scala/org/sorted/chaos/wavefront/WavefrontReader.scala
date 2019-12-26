package org.sorted.chaos.wavefront

import org.joml.Vector3f
import org.sorted.chaos.wavefront.mesh.{ IndexMesh, Mesh, SimpleIndexMesh, SimpleMesh }
import org.sorted.chaos.wavefront.reader.{ Material, Wavefront }
import org.sorted.chaos.wavefront.utilities.{ FileReader, Timer }

object WavefrontReader {

  /**
    * This method creates a [[org.sorted.chaos.wavefront.mesh.Mesh]] with
    *  - vertices
    *  - textures (optional)
    *  - normals (optional)
    * from an .obj file
    * @param filename the .obj file (represents the input data)
    * @return a [[org.sorted.chaos.wavefront.mesh.Mesh]] with
    *          - Array[Float] for vertices
    *          - Array[Float] for textures (is empty, if file does not contain this data type)
    *          - Array[Float] for normals (is empty, if file does not contain this data type)
    */
  def from(filename: String): Mesh = {
    val wavefront = getWavefront(filename)
    val t1        = Timer.start
    val result    = Mesh.from(wavefront)
    Timer.end(t1, "Create the mesh from the wavefront")
    result
  }

  /**
    * This method creates a [[org.sorted.chaos.wavefront.mesh.IndexMesh]] with
    *  - vertices
    *  - textures (optional)
    *  - normals (optional)
    *  - indexes
    * from an .obj file. It can be used for OpenGL IndexDrawing.
    *
    * @param filename the .obj file (represents the input data)
    * @return a [[org.sorted.chaos.wavefront.mesh.IndexMesh]] with
    *          - Array[Float] for vertices
    *          - Array[Float] for textures (is empty, if file does not contain this data type)
    *          - Array[Float] for normals (is empty, if file does not contain this data type)
    *          - Array[Int] the index list for IndexDrawing
    */
  def withIndexFrom(filename: String): IndexMesh = {
    val wavefront = getWavefront(filename)
    IndexMesh.from(wavefront)
  }

  /**
    * This method creates a [[org.sorted.chaos.wavefront.mesh.SimpleMesh]] with
    *  - vertices
    *  - color
    * from an .obj file.
    *
    * @param filename the .obj file (represents the input data)
    * @return a [[org.sorted.chaos.wavefront.mesh.SimpleMesh]] with
    *          - Array[Float] for vertices
    *          - Array[Float] for colors (of each vertex, will always be the same color)
    */
  def simpleFrom(filename: String, color: Vector3f): SimpleMesh = {
    val wavefront = getWavefront(filename)
    SimpleMesh.from(wavefront, color)
  }

  /**
    * This method creates a [[org.sorted.chaos.wavefront.mesh.SimpleIndexMesh]] with
    *  - vertices
    *  - color
    *  - indexes
    * from an .obj file. It can be used for OpenGL IndexDrawing.
    *
    * @param filename the .obj file (represents the input data)
    * @return a [[org.sorted.chaos.wavefront.mesh.SimpleIndexMesh]] with
    *          - Array[Float] for vertices
    *          - Array[Float] for colors (of each vertex, will always be the same color)
    *          - Array[Int] the index list for IndexDrawing
    */
  def simpleWithIndexFrom(filename: String, color: Vector3f): SimpleIndexMesh = {
    val wavefront = getWavefront(filename)
    SimpleIndexMesh.from(wavefront, color)
  }

  /**
    * This method creates a [[org.sorted.chaos.wavefront.reader.Material]] with
    *   - ambient color
    *   - diffuse color
    *   - specular color
    *   - specular exponent
    *   from a .mtl file.
    * @param filename the .mtl file (represents the input data)
    * @return a [[org.sorted.chaos.wavefront.reader.Material]] with
    *          - Vector3f for ambient color
    *          - Vector3f for diffuse color
    *          - Vector3f for specular color
    *          - Float for specular exponent
    */
  def materialFrom(filename: String): Material = {
    val lines = FileReader.read(filename)
    Material.from(lines)
  }

  private def getWavefront(filename: String) = {
    val t1    = Timer.start
    val lines = FileReader.read(filename)
    Timer.end(t1, "Read the file")
    val t2     = Timer.start
    val result = Wavefront.from(lines)
    Timer.end(t2, "Create a Wavefront")
    result
  }
}
