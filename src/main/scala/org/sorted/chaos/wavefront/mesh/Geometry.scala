package org.sorted.chaos.wavefront.mesh

import org.joml.{ Vector2f, Vector3f }
import org.sorted.chaos.wavefront.reader.Triangle

trait Geometry {

  protected def getTexturesOfTriangle(triangle: Triangle, textures: Vector[Vector2f]): Vector[Float] = {
    val indices = triangle.indices

    (indices(0).textureIndex, indices(1).textureIndex, indices(2).textureIndex) match {
      case (Some(t1), Some(t2), Some(t3)) =>
        // we have to subtract one because .obj index starts from 1, Scala Collection index starts from 0
        val texture1 = textures(t1 - 1)
        val texture2 = textures(t2 - 1)
        val texture3 = textures(t3 - 1)

        Vector(
          texture1.x,
          texture1.y,
          texture2.x,
          texture2.y,
          texture3.x,
          texture3.y
        )
      case _ => Vector.empty[Float]
    }
  }

  protected def getVerticesOfTriangle(triangle: Triangle, vertices: Vector[Vector3f]): Vector[Float] = {
    val indices = triangle.indices

    // we have to subtract one because .obj index starts from 1, Scala Collection index starts from 0
    val point1 = vertices(indices(0).vertexIndex - 1)
    val point2 = vertices(indices(1).vertexIndex - 1)
    val point3 = vertices(indices(2).vertexIndex - 1)

    Vector(
      point1.x,
      point1.y,
      point1.z,
      point2.x,
      point2.y,
      point2.z,
      point3.x,
      point3.y,
      point3.z
    )
  }

  protected def getNormalsOfTriangle(triangle: Triangle, normals: Vector[Vector3f]): Vector[Float] = {
    val indices = triangle.indices

    (indices(0).normalIndex, indices(1).normalIndex, indices(2).normalIndex) match {
      case (Some(n1), Some(n2), Some(n3)) =>
        // we have to subtract one because .obj index starts from 1, Scala Collection index starts from 0
        val point1 = normals(n1 - 1)
        val point2 = normals(n2 - 1)
        val point3 = normals(n3 - 1)

        Vector(
          point1.x,
          point1.y,
          point1.z,
          point2.x,
          point2.y,
          point2.z,
          point3.x,
          point3.y,
          point3.z
        )
      case _ => Vector.empty[Float]
    }
  }

  protected def getColorOfTriangle(color: Vector3f): Vector[Float] =
    Vector(
      color.x,
      color.y,
      color.z,
      color.x,
      color.y,
      color.z,
      color.x,
      color.y,
      color.z
    )
}
