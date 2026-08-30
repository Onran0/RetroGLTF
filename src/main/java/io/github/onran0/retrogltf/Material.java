package io.github.onran0.retrogltf;

public class Material {
    private GLTexture diffuse;

    public Material(GLTexture diffuse) {
        if(diffuse == null)
            throw new IllegalArgumentException("diffuse == null");

        this.diffuse = diffuse;
    }

    public GLTexture getDiffuse() {
        return diffuse;
    }

    public void set(Material material) {
        this.diffuse = material.getDiffuse();
    }
}