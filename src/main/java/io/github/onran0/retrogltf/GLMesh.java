package io.github.onran0.retrogltf;

public class GLMesh {
    private final GLMeshPrimitive[] primitives;

    public GLMesh(GLMeshPrimitive[] primitives) {
        this.primitives = primitives;
    }

    public GLMeshPrimitive[] getPrimitives() {
        return primitives;
    }
}