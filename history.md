# History

Here you can see some documentation about the project history.

#### 2019-11-22
I read how to publish a lib locally for testing the wavefront-reader.
I modified the `build.sbt` and now I can use `sbt publishLocal` for this.
I also tested with if a simple cube created with blender and exported as .obj file, is correctly transformed.
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
