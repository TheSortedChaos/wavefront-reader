# History

Here you can see some documentation about the project history.

#### 2019-11-21
Finally I got the code-coverage import from [Travis-CI][travis-ci] into [SonarCloud][sonarcloud] working.
I started working on transforming the wavefront (internal model) to something I can use with [OpenGL][opengl].

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
