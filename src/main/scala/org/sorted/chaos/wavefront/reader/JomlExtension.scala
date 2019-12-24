package org.sorted.chaos.wavefront.reader

import org.joml.{ Vector2f, Vector3f }

object JomlExtension {

  implicit class Vector3fExtension(val vector: Vector3f) {
    def toArray: Array[Float] = Array(vector.x, vector.y, vector.z)
  }

  implicit class Vector2fExtension(val vector: Vector2f) {
    def toArray: Array[Float] = Array(vector.x, vector.y)
  }
}
