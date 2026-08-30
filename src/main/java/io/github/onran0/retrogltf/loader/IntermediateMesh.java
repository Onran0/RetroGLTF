package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLMesh;

import java.util.Map;

class IntermediateMesh {
    private final GLMesh glMesh;
    private final Map<Integer, Integer> globalMaterialIndexToLocalMap;

    public IntermediateMesh(GLMesh glMesh, Map<Integer, Integer> globalMaterialIndexToLocalMap) {
        this.glMesh = glMesh;
        this.globalMaterialIndexToLocalMap = globalMaterialIndexToLocalMap;
    }

    public GLMesh getGLMesh() {
        return glMesh;
    }

    public Map<Integer, Integer> getGlobalMaterialIndexToLocalMap() {
        return globalMaterialIndexToLocalMap;
    }
}