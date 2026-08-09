package io.github.onran0.retrogltf;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class GLMesh {
    private final int vbo;
    private final int vao;
    private final int ebo;
    private final int frontFaceMode;

    public GLMesh(int vbo, int vao, int ebo, int frontFaceMode) {
        this.vbo = vbo;
        this.vao = vao;
        this.ebo = ebo;
        this.frontFaceMode = frontFaceMode;
    }

    public boolean hasEBO() {
        return this.ebo != -1;
    }

    public int getVBO() {
        return this.vbo;
    }

    public int getVAO() {
        return this.vao;
    }

    public int getEBO() {
        return this.ebo;
    }

    public int getFrontFaceMode() {
        return this.frontFaceMode;
    }

    public void destroy() {
        GL30.glDeleteVertexArrays(this.vao);

        if(hasEBO())
            GL15.glDeleteBuffers(this.ebo);

        GL15.glDeleteBuffers(this.vbo);
    }
}