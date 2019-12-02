package org.sorted.chaos.wavefront.mesh

import org.sorted.chaos.wavefront.reader.{ Indices, Point, UVCoordinate, Wavefront }

object HandleIndexing {

  final case class Accumulator(
      currentIndex: Int,
      lookUpTable: Map[Indices, Int],
      vertices: Vector[Point],
      textures: Vector[UVCoordinate],
      index: Vector[Int]
  )

  private def emptyAccumulator =
    Accumulator(
      0,
      Map.empty[Indices, Int],
      Vector.empty[Point],
      Vector.empty[UVCoordinate],
      Vector.empty[Int]
    )

  def doit2(wavefront: Wavefront): (Vector[Point], Vector[UVCoordinate], Vector[Int]) = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTextures  = wavefront.textures
    val wavefrontTriangles = wavefront.triangles

    val result = wavefrontTriangles.flatMap(_.asVector).foldLeft(emptyAccumulator) { (accumulator, indices) =>
      {
        if (accumulator.lookUpTable.contains(indices)) {
          val newIndex = accumulator.lookUpTable(indices)
          accumulator.copy(index = accumulator.index :+ newIndex)
        } else {
          accumulator.copy(
            currentIndex = accumulator.currentIndex + 1,
            lookUpTable  = accumulator.lookUpTable + (indices -> accumulator.currentIndex),
            vertices     = accumulator.vertices :+ wavefrontVertices(indices.vertexIndex - 1),
            textures     = accumulator.textures :+ wavefrontTextures(indices.textureIndex.get - 1),
            index        = accumulator.index :+ accumulator.currentIndex
          )
        }
      }
    }
    (result.vertices, result.textures, result.index)
  }

  def doit(wavefront: Wavefront): (Vector[Point], Vector[UVCoordinate], Vector[Int]) = {
    val wavefrontVertices  = wavefront.vertices
    val wavefrontTextures  = wavefront.textures
    val wavefrontTriangles = wavefront.triangles

    var currentIndex   = 0
    var lookUpForIndex = Map.empty[Indices, Int]
    var vertices       = Vector.empty[Point]
    var textures       = Vector.empty[UVCoordinate]
    var index          = Vector.empty[Int]

    for (triangle <- wavefrontTriangles) {
      for (indices <- triangle.asVector) {

        if (lookUpForIndex.contains(indices)) {
          val i = lookUpForIndex(indices)
          index = index :+ i
        } else {
          lookUpForIndex = lookUpForIndex + (indices -> currentIndex)
          vertices       = vertices :+ wavefrontVertices(indices.vertexIndex - 1)
          textures       = textures :+ wavefrontTextures(indices.textureIndex.get - 1)
          index          = index :+ currentIndex

          currentIndex = currentIndex + 1
        }
      }
    }
    (vertices, textures, index)
  }

}
