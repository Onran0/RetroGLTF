package io.github.onran0.retrogltf.loader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

enum ImageColorModel {
    RGB(GL11.GL_RGB8, GL11.GL_RGB),
    RGBA(GL11.GL_RGBA8, GL11.GL_RGBA),
    G(GL30.GL_R8, GL11.GL_RED),
    GA(GL30.GL_RG8, GL30.GL_RG);

    private final int internalGlFormat;
    private final int glFormat;

    ImageColorModel(int internalGlFormat, int glFormat) {
        this.internalGlFormat = internalGlFormat;
        this.glFormat = glFormat;
    }

    public int getInternalGLFormat() {
        return internalGlFormat;
    }

    public int getGLFormat() {
        return this.glFormat;
    }
}