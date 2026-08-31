package io.github.onran0.retrogltf;

public class Material {
    private GLTexture diffuse;
    private boolean useCulling;

    public Material(GLTexture diffuse, boolean useCulling) {
        if(diffuse == null)
            throw new IllegalArgumentException("diffuse == null");

        this.diffuse = diffuse;
        this.useCulling = useCulling;
    }

    public GLTexture getDiffuse() {
        return diffuse;
    }

    public boolean isShouldUseCulling() {
        return useCulling;
    }

    public void set(Material material) {
        this.diffuse = material.getDiffuse();
        this.useCulling = material.useCulling;
    }
}