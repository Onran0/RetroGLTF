package io.github.onran0.retrogltf.loader;

import org.lwjgl.opengl.GL11;

enum ImageColorModel {
    RGB(GL11.GL_RGB),
    RGBA(GL11.GL_RGBA),;

    private final int glType;

    ImageColorModel(int glType) {
        this.glType = glType;
    }

    public int getGLType() {
        return this.glType;
    }
}