# RetroGLTF

**RetroGLTF** - is a library for **LWJGL 2** for parsing, loading and render **glTF 2.0** models
(including .glb).

## Building

### Library

Output `.jar` will appear in project `./target` subdirectory after this command execution:

`mvn clean package`

### Test

Command for visual tests of parser and renderer:

`mvn -Prender-test clean test-compile exec:java`