package io.github.onran0.retrogltf;

import java.util.Objects;

public class Material {
    private TextureInfo baseColor;
    private boolean useCulling;

    public Material(TextureInfo baseColor, boolean useCulling) {
        if(baseColor == null)
            throw new IllegalArgumentException("baseColor == null");

        this.baseColor = baseColor;
        this.useCulling = useCulling;
    }

    public TextureInfo getBaseColor() {
        return baseColor;
    }

    public boolean isShouldUseCulling() {
        return useCulling;
    }

    public void set(Material material) {
        this.baseColor = material.getBaseColor();
        this.useCulling = material.useCulling;
    }

    public void free() {
        this.baseColor.getTexture().free();
    }

    public int hashCode() {
        if(baseColor != null) {
            return Objects.hash(baseColor.getTexCoordIndex(), true);
        } else {
            return 0;
        }
    }
}