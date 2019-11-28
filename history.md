# History

Here you can see some documentation about the project history.

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
