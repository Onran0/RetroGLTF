package io.github.onran0.retrogltf;

import org.lwjgl.opengl.GL11;

public class GLTexture {
    public static GLTexture MISSING = new GLTexture(0);

    private final int textureId;

    public GLTexture(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureID() {
        return textureId;
    }

    public void destroy() {
        GL11.glDeleteTextures(textureId);
    }
}