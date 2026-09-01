package io.github.onran0.retrogltf;

public class TextureInfo {
    private final GLTexture texture;
    private final int texCoordIndex;

    public TextureInfo(GLTexture texture, int texCoordIndex) {
        if(texture == null)
            throw new IllegalArgumentException("texture == null");

        if(texCoordIndex < 0)
            throw new IllegalArgumentException("texCoordIndex must be positive");

        this.texture = texture;
        this.texCoordIndex = texCoordIndex;
    }

    public GLTexture getTexture() {
        return texture;
    }

    public int getTexCoordIndex() {
        return texCoordIndex;
    }
}