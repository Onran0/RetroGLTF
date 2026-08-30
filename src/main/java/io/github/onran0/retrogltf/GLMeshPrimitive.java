package io.github.onran0.retrogltf;

public class GLMeshPrimitive {
    private final int vao;
    private final int vbo;

    private final int ebo;
    private final int eboIndicesType;

    private final int elementsType;

    private final int materialIndex;

    public GLMeshPrimitive(
            int vao, int vbo,
            int ebo, int eboIndicesType,
            int elementsType,
            int materialIndex
    ) {
        this.vao = vao;
        this.vbo = vbo;

        this.ebo = ebo;
        this.eboIndicesType = eboIndicesType;

        this.elementsType = elementsType;

        this.materialIndex = materialIndex;
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

    public int getEBOIndicesType() {
        return this.eboIndicesType;
    }

    public int getElementsType() {
        return this.elementsType;
    }

    public int getMaterialIndex() {
        return this.materialIndex;
    }
}