[![codecov](https://codecov.io/gh/TheSortedChaos/wavefront-reader/branch/master/graph/badge.svg)](https://codecov.io/gh/TheSortedChaos/wavefront-reader)
# wavefront-reader
The _wavefront-reader_ can read [.obj][wavefront] files and transform then into usable data for [OpenGL][opengl] (`Array[Float]`, `Array[Int]`)
Wavefront (.obj) files are used to store geometric data, like geometric objects build with [Blender][blender].
These files are read, validated and then get transformed into an internal model.
In the next step this internal model is transformed to a `Mesh` object.
There are several kinds of Meshes, like:

| Mesh Type             |  Provided Data                                                            | 
|:----------------------|:--------------------------------------------------------------------------|
| `SimpleMesh`          | `vertices: Array[Float]`, `color: Array[Float]`                           |
| `SimpleIndexMesh`     | `vertices: Array[Float]`, `color: Array[Float]`, `indexes: Array[Int]`    |
| `TexturedMesh`        | `vertices: Array[Float]`, `textures: Array[Float]`                        |
| `TexturedIndexedMesh` | `vertices: Array[Float]`, `textures: Array[Float]`, `indexes: Array[Int]` |
 
These `Mesh` objects can than be used for filling a [VertexBufferObject][vertexBufferObject] from OpenGL (e.g. with [LWJGL][lwjgl])

## Requirements
* Scala 2.13.1
* SBT 1.3.3
* Java JDK 1.8

### TODOs
- add log messages
- update readme
  - exmplain indexed mesh vs normal
  - example how to create a mesh
  - explain fromResource vs fromFile
- FileReader should take files(because otherwise it has to be in resources)
  - fromResource
    - createSimpleIndexMeshFromFile
    - createSimpleIndexMeshFromResource
  - fromFile
- remove biTangents from Mesh and IndexedMesh? tangents needed in SGE
- check the documentation (comments in code)
- create documentation plantUml?
- interpolate normals?
- normal mapping (better math (BigDecimals?))
- Timer measure stuff --> with LogMessages
- add License (open source Apache 2.0?)
- central maven release
- remove normal mapping? -> check with simple grafix enngine
  - remove tangents and biTangents from Mesh and IndexedMesh
- add sonacube analysis and badge
  
  
[blender]: https://www.blender.org/
[lwjgl]: https://www.lwjgl.org/
[opengl]: https://www.opengl.org/
[vertexBufferObject]: https://en.wikipedia.org/wiki/Vertex_buffer_object
[wavefront]: https://en.wikipedia.org/wiki/Wavefront_.obj_file
