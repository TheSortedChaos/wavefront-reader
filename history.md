# History

#### 2019-12-16
Houston we got a problem during reading a 3 MB file:
```
Read the file took 167 ms (= 0 s)
Create a Wavefront took 737 ms (= 0 s)
Create the mesh from the wavefront took 9004 ms (= 9 s)
Create Normal Mapping took 24860 ms (= 24 s)
```

#### 2019-12-21
I refactored the whole result classes (the Meshes).
Instead of having one class for each type now there is only one type.
If the data unit in the .obj file is not present the array will be empty in the result data structure.
I created `WavefrontReader` object as entry point for the framework user.
It provides the following methods:
* `from(filename: String): Mesh`
* `withIndexFrom(filename: String): IndexMesh`
* `simpleFrom(filename: String, color: Color): SimpleMesh`
* `simpleWithIndexFrom(filename: String, color: Color): SimpleIndexMesh` 

#### 2019-12-14
I added the material file reader for .mtl files. 
Now it is possible to read a material file for light processing.
At the moment it is only an internal model, because I am not sure how I will need the data for the shader in OpenGL.
I also thought about the validation process of the wavefront file (.obj) and I am not happy with it anymore.
I refactored the validation process for reading the wavefront (.obj file).
Now it looks like the material file (.mtl) validation.
I decided to get rid of the different Mesh types.
I want to use one type and only fill it with data, that is present.
I build up a little prototype for it.

#### 2019-12-12
Added support for creating meshes from a wavefront with vertices, textures and normals, but without index (for OpenGl IndexDrawing).
Test was also added.
* `TexturedNormalMesh(vertices: Array[Float], textures: Array[Float], normals: Array[Float])`

Now, loading a Mesh and apply light to it, should work. (I Have to test it, yet).

#### 2019-12-03
I used the prototype and embedded it into the code.
Some refactoring happened and missing tests were added.
I am not fully happy with the architecture, `SimpleMesh`, `TexturedMesh` and the indexed versions are not linked and stand there as own classes/objects.
Not sure which pattern I can apply here.
I  extracted some helper function into a `Mesh` trait, but this trait is only be used for non-indexed versions.
The problem at the moment is the following: I have:

* `SimpleMesh(vertices: Array[Float], color: Array[Float])`
* `SimpleIndexedMesh(vertices: Array[Float], color: Array[Float], indexes: Float[Int]`
* `TexturedMesh(vertices: Array[Float], textures; Array[Float]`
* `TexturedIndexedMesh(vertices: Array[Float], textures: Array[Float], indexes: Float[Int]`

but I dont have a (good) level of abstraction.
This problem is hopefully solved in a later refactoring ;-/

#### 2019-12-02
I added a prototype function for re-ordering vertices and textures for index drawing

#### 2019-11-30
I added the missing tests, restructure some more packages and added the `SimpleTextureMesh` which can be created from a wavefront.

#### 2019-11-29
I refactored the whole error handling, extracted some objects/methods and restructured the packages.
Triangle validation is done.
Tests are still missing.
Logging is still missing.

#### 2019-11-28
I added error handling for .obj file reading.
Now if something cannot parsed correctly it will be added to a list of errors.
This list of errors is later used to create an `IllegalArgumentException`.
The .obj file reading is now returning a `Try` (`Success(wavefront)` or `Failure(exception)`).
Triangle validation is still missing.
Tests are still missing.

#### 2019-11-27
I added the smooth group to the `Wavefront` data structure. 
I assume I will need it later (when light is involved) for gouraud shading vs phong shading.
I also refactored the process of creating the `Mesh`.
Reading .obj files is done for every type I probably will need.
At the moment only `SimpleMesh` and `SimpleIndexedMesh` is possible to create from an .obj file, the other ones will follow soon.

#### 2019-11-25
I added some tests for the wavefront read file process.

#### 2019-11-24
I modified the read .obj file / create `Wavefront` process.
Triangulated geometry with the following definitions are now supported:
* vertex - face
* vertex - normal - face
* vertex - texture - face
* vertex - texture - normal - face

Tests are missing, yet.
Transformation from `Wavefront` to Mesh is missing, yet.
I'm not really happy with the rewrite (No error handling, no validation, not so nice code).

#### 2019-11-22
I read how to publish a lib locally for testing the wavefront-reader.
I modified the `build.sbt` and now I can use `sbt publishLocal` for this.
I also tested, if a simple cube created with blender and exported as .obj file, is correctly transformed.
Therefore I used a small LWJGL project, added the wavefront-reader dependency, read the .obj file and generated:
* `SimpleMesh`
* `SimpleIndexedMesh`

The visualizations looked correct.

#### 2019-11-21
Finally I got the code-coverage import from [Travis-CI][travis-ci] into [SonarCloud][sonarcloud] working.
I started working on transforming the wavefront (internal model) to something I can use with [OpenGL][opengl].
This transformation was added for the simplest wavefront (vertices and faces). 
I also added a transformation for a simple wavefront to an indexed Mesh (used for indexed drawing in OpenGL)

#### 2019-11-20
I decided to play around with [LightWeight Java Game Engine][lwjgl] once again.
Therefore I need some data structure to represent some geometry data.
Because I need some tools like [Blender][blender] to create geometry data, I need _something_ (a data structure) between Blender and a LWJGL-project.
This _something_ will be: the [wavefront file format][wavefront] ([Wikipedia][wavefront-wiki]).
It is:
* human readable
* easy to understand/implement
* Blender can export to that file format

Today I set up the repository and build pipeline with analysis.
I created the functionality to read the simplest .obj file, containing only vertices and faces. 

[blender]: https://www.blender.org/
[lwjgl]: https://www.lwjgl.org/
[opengl]: https://www.opengl.org/
[sonarcloud]: https://sonarcloud.io/dashboard?id=mwttg_wavefront-reader
[travis-ci]: https://github.com/mwttg/wavefront-reader
[wavefront]: http://paulbourke.net/dataformats/obj/
[wavefront-wiki]: https://en.wikipedia.org/wiki/Wavefront_.obj_file
