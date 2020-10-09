# ToDo - List

This is the todo list.
My second brain for not-forgetting stuff.

- add log messages
- update readme
  - explain indexed mesh vs normal
  - explain fromResource vs fromFile (when added)
- FileReader should take files (because otherwise it has to be in resources)
  - fromResource
    - createSimpleIndexMeshFromResource
  - fromFile
    - createSimpleIndexMeshFromFile
- remove biTangents from Mesh and IndexedMesh? tangents needed in SGE
- check the documentation (comments in code)
- create documentation plantUml?
- normal mapping (better math (BigDecimals?))
- Timer measure stuff --> with LogMessages
- add License (open source Apache 2.0?)
- central maven release
- remove normal mapping?
  - using tangents with Mesh without Index works (biTangents calculated in Shader)
  - will not work for IndexMesh
- add sonacube analysis and badge
- cross platform compile/release?
