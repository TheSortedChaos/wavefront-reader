[![codecov](https://codecov.io/gh/TheSortedChaos/wavefront-reader/branch/master/graph/badge.svg)](https://codecov.io/gh/TheSortedChaos/wavefront-reader)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=wavefront-reader&metric=alert_status)](https://sonarcloud.io/dashboard?id=wavefront-reader)

# wavefront-reader
The _wavefront-reader_ can read [.obj][wavefront] files and transform then into usable data for [OpenGL][opengl] (`Array[Float]`, `Array[Int]`)
Wavefront (.obj) files are used to store geometric data, like geometric objects build with e.g. [Blender][blender].
These files are read, validated and then get transformed into an internal model.
In the next step this internal model is transformed to a `Mesh` object.
There are several kinds of Meshes, like:

| Mesh Type             |  Provided Data                                                            | 
|:----------------------|:--------------------------------------------------------------------------|
| `SimpleMesh`          | `vertices: Array[Float]`, `color: Array[Float]`                           |
| `SimpleIndexMesh`     | `vertices: Array[Float]`, `color: Array[Float]`, `indexes: Array[Int]`    |
| `Mesh`                | `vertices: Array[Float]`, `textures: Array[Float]`, `normals: Array[Float]` and optional `tangents: Array[Float]`, `biTangents: Array[Float]` for NormalMapping|
| `IndexedMesh`         | `vertices: Array[Float]`, `textures: Array[Float]`, `indexes: Array[Int]` |
 
These `Mesh` objects can than be used for filling a [VertexBufferObject][vertexBufferObject] from OpenGL (e.g. with [LWJGL][lwjgl])

## Requirements
* Scala 2.13.3
* SBT 1.3.3
* Java JDK 1.8

## How to use

Add the dependency to your `build.sbt` like:
```
libraryDependencies += "com.github.thesortedchaos" %% "wavefront-reader" % "0.1.0"
```
or
```
libraryDependencies += "com.github.thesortedchaos" % "wavefront-reader_2.13" % "0.1.0"
```
After this you have the option to read an `.obj` file or `.mtl` file from your resource folder of your project.
Reading a file from somewhere else is not supported at the moment.
You have the following options:

### Read a geometry (.obj) file
```
import de.sorted.chaos.wavefront.WavefrontReader
import de.sorted.chaos.wavefront.mesh.Mesh

...

val mesh: Mesh = WavefrontReader.from("my-example.obj")
```
This will create a `mesh` model where the following data will be included.
Only if the data is present in the `.obj` file, otherwise the Array will be empty.
* `vertices: Array[Float]`
* `textures: Array[Float]`
* `normals: Array[Float]`

The following Arrays will be empty (used for normal mapping)
* `tangents: Array[Float]`
* `biTangents: Array[Float]`

There are also some experimental methods, you can play around with:
* `WavefrontReader.withNormalMappingFrom(filename: String): Mesh` - This will create a `Mesh` with tangents and biTangents data for normal mapping
* `WavefrontReader.withIndexFrom(filename: String): IndexMesh` - This will create a `IndexMesh` (without data for normal mapping) which can be used for OpenGL IndexDrawing
* `WavefrontReader.simpleFrom(filename: String, color: Vector3f): SimpleMesh` - This will create a `SimpleMesh` containing only vertices and the color.
* `WavefrontReader.simpleWithIndexFrom(filename: String, color: Vector3f): SimpleIndexMesh` - This will create a `SimpleIndexMesh` the indexed version of a `SimpleMesh` for OpenGL IndexDrawing

### Read a material (.mtl) file
```
import de.sorted.chaos.wavefront.WavefrontReader
import e.sorted.chaos.wavefront.reader.Material

...

val material: Material = WavefrontReader.materialFrom("my-example.mtl")
```
This will create a `material` model where the following data will be included:
* `ambientColor: Vector3f`
* `diffuseColor: Vector3f`
* `specularColor: Vector3f`
* `specularExponent: Float`

`Vector3f` is coming from [JOML - Java OpenGL Math Library][joml]

![Cube][rotating-cube]

###### Social Media

[![Twitter][twitter-icon]][twitter-account]       [![Instagram][instagram-icon]][instagram-account]       [![YouTube][youtube-icon]][youtube-playlist]
  
[comment]: <> (collection of links sorted alphabetically ascending)
[blender]: https://www.blender.org/
[rotating-cube]: documentation/images/rotating-textured-cube.gif
[joml]: https://github.com/JOML-CI/JOML
[lwjgl]: https://www.lwjgl.org/
[opengl]: https://www.opengl.org/
[vertexBufferObject]: https://en.wikipedia.org/wiki/Vertex_buffer_object
[wavefront]: https://en.wikipedia.org/wiki/Wavefront_.obj_file
[comment]: <> (social media)
[instagram-account]: https://www.instagram.com/the.sorted.chaos/
[instagram-icon]: documentation/images/social-media/instagram-icon.png
[twitter-account]: https://twitter.com/sorted_chaos
[twitter-icon]: documentation/images/social-media/twitter-icon.png
[youtube-icon]: documentation/images/social-media/youtube-icon.png
[youtube-playlist]: https://www.youtube.com/channel/UCV50VCpRGU6yst-si72t_tA/playlists
