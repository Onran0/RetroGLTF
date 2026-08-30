package io.github.onran0.retrogltf;

public class GLTexture {
    public static GLTexture MISSING = new GLTexture(0);

    private final int textureId;

    public GLTexture(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureID() {
        return textureId;
    }
}