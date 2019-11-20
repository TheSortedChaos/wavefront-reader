[![Build Status](https://travis-ci.com/mwttg/wavefront-reader.svg?branch=master)](https://travis-ci.com/mwttg/wavefront-reader)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mwttg_wavefront-reader&metric=alert_status)](https://sonarcloud.io/dashboard?id=mwttg_wavefront-reader)
# wavefront-reader
This is a little project to read wavefront (.obj) files. 
Wavefront file are used to store geometric data, like meshes from [Blender][blender].
After exporting your data from Blender, this application takes the export file and creates an internal wavefront data structure.
This internal data structure is then transformed and can be used with OpenGL (e.g. [LWJGL][lwjgl])

## Requirements
* Scala 2.13.1
* SBT 1.3.3
* Java JDK 1.8

: TODO fill

[blender]: https://www.blender.org/
[lwjgl]: https://www.lwjgl.org/
