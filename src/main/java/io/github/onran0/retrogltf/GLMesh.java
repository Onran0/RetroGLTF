package io.github.onran0.retrogltf;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class GLMesh {
    private final GLMeshPrimitive[] primitives;

    public GLMesh(GLMeshPrimitive[] primitives) {
        this.primitives = primitives;
    }

    public GLMeshPrimitive[] getPrimitives() {
        return primitives;
    }

    public void free() {
        for(GLMeshPrimitive prim : this.primitives) {
            GL15.glDeleteBuffers(prim.getVBO());

            if(prim.hasEBO())
                GL15.glDeleteBuffers(prim.getEBO());

            GL30.glDeleteVertexArrays(prim.getVAO());
        }
    }
}