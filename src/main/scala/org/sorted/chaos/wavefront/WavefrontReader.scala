package org.sorted.chaos.wavefront

import org.sorted.chaos.wavefront.mesh.{ IndexMesh, Mesh, SimpleIndexMesh, SimpleMesh }
import org.sorted.chaos.wavefront.reader.{ Color, Wavefront, WavefrontMaterial }
import org.sorted.chaos.wavefront.utilities.FileReader

object WavefrontReader {

  /**
    * This method creates a [[Mesh]] with
    *  - vertices
    *  - textures (optional)
    *  - normals (optional)
    * from an .obj file
    * @param filename the .obj file (represents the input data)
    * @return a [[Mesh]] with
    *          - Array[Float] for vertices
    *          - Array[Float] for textures (is empty, if file does not contain this data type)
    *          - Array[Float] for normals (is empty, if file does not contain this data type)
    */
  def from(filename: String): Mesh = {
    val wavefront = getWavefront(filename)
    Mesh.from(wavefront)
  }

  /**
    * This method creates a [[IndexMesh]] with
    *  - vertices
    *  - textures (optional)
    *  - normals (optional)
    *  - indexes
    * from an .obj file. It can be used for OpenGL IndexDrawing.
    *
    * @param filename the .obj file (represents the input data)
    * @return a [[IndexMesh]] with
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
    * This method creates a [[SimpleMesh]] with
    *  - vertices
    *  - color
    * from an .obj file.
    *
    * @param filename the .obj file (represents the input data)
    * @return a [[SimpleMesh]] with
    *          - Array[Float] for vertices
    *          - Array[Float] for colors (of each vertex, will always be the same color)
    */
  def simpleFrom(filename: String, color: Color): SimpleMesh = {
    val wavefront = getWavefront(filename)
    SimpleMesh.from(wavefront, color)
  }

  /**
    * This method creates a [[SimpleIndexMesh]] with
    *  - vertices
    *  - color
    *  - indexes
    * from an .obj file.  It can be used for OpenGL IndexDrawing.
    *
    * @param filename the .obj file (represents the input data)
    * @return a [[SimpleIndexMesh]] with
    *          - Array[Float] for vertices
    *          - Array[Float] for colors (of each vertex, will always be the same color)
    *          - Array[Int] the index list for IndexDrawing
    */
  def simpleWithIndexFrom(filename: String, color: Color): SimpleIndexMesh = {
    val wavefront = getWavefront(filename)
    SimpleIndexMesh.from(wavefront, color)
  }

  /**
   * TODO fill it!!!
   * @param filename
   * @return
   */
  def materialFrom(filename: String): WavefrontMaterial = {
    val lines = FileReader.read(filename)
    // perhaps I need another cae class (color as array
    WavefrontMaterial.from(lines)
  }

  private def getWavefront(filename: String) = {
    val lines = FileReader.read(filename)
    Wavefront.from(lines)
  }
}
