package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.{ Point, Triangle, UVCoordinate }

/**
  * This model class represents the color of the Mesh (when not having a texture)
  *
  * @param red the amount of red a value between 0 and 1
  * @param green the amount of green a value between 0 and 1
  * @param blue the amount of blue a value between 0 and 1
  */
final case class SolidColor(red: Float, green: Float, blue: Float) {
  def toArray: Array[Float] = Array(red, green, blue)
}

trait Mesh {

  def getTexturesOfTriangle(triangle: Triangle, textures: Vector[UVCoordinate]): Array[Float] = {
    val indices = triangle.indicesToVector

    // we have to subtract one because .obj index starts from 1, Scala Collection index starts from 0
    val texture1 = textures(indices(0).textureIndex.get - 1)
    val texture2 = textures(indices(1).textureIndex.get - 1)
    val texture3 = textures(indices(2).textureIndex.get - 1)

    texture1.toArray ++ texture2.toArray ++ texture3.toArray
  }

  def getVerticesOfTriangle(triangle: Triangle, vertices: Vector[Point]): Array[Float] = {
    val indices = triangle.indicesToVector

    // we have to subtract one because .obj index starts from 1, Scala Collection index starts from 0
    val point1 = vertices(indices(0).vertexIndex - 1)
    val point2 = vertices(indices(1).vertexIndex - 1)
    val point3 = vertices(indices(2).vertexIndex - 1)

    point1.toArray ++ point2.toArray ++ point3.toArray
  }

  def getNormalsOfTriangle(triangle: Triangle, normals: Vector[Point]): Array[Float] = getVerticesOfTriangle(triangle, normals)

  def getColorOfTriangle(color: SolidColor): Array[Float] =
    Array(
      color.red,
      color.green,
      color.blue,
      color.red,
      color.green,
      color.blue,
      color.red,
      color.green,
      color.blue
    )
}
