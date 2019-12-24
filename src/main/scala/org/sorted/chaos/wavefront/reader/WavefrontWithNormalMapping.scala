package org.sorted.chaos.wavefront.reader

import org.joml.{ Vector2f, Vector3f }

final case class WavefrontWithNormalMapping(
    vertices: Vector[Vector3f],
    triangles: Vector[Triangle],
    normals: Vector[Vector3f],
    tangents: Vector[Vector3f],
    biTangents: Vector[Vector3f],
    textures: Vector[Vector2f]
)

// https://www.youtube.com/watch?v=4FaWLgsctqY
object WavefrontWithNormalMapping {
  def empty: WavefrontWithNormalMapping =
    WavefrontWithNormalMapping(
      vertices   = Vector.empty[Vector3f],
      triangles  = Vector.empty[Triangle],
      normals    = Vector.empty[Vector3f],
      tangents   = Vector.empty[Vector3f],
      biTangents = Vector.empty[Vector3f],
      textures   = Vector.empty[Vector2f]
    )

//  def generateFrom(wavefront: Wavefront) =
//    wavefront.triangles.foldLeft(WavefrontWithNormalMapping.empty) { (accumulator, triangle) =>
//      {}
//    }
}
