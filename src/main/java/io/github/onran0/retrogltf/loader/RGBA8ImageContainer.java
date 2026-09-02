package io.github.onran0.retrogltf.loader;

import java.nio.ByteBuffer;

class RGBA8ImageContainer {
    private final int width;
    private final int height;
    private final ImageColorModel colorModel;
    private final ByteBuffer buffer;
    private final boolean bufferFromPool;

    public RGBA8ImageContainer(
            final int width, final int height, ImageColorModel colorModel,
            final ByteBuffer buffer, final boolean bufferFromPool
    ) {
        this.width = width;
        this.height = height;
        this.colorModel = colorModel;

        this.buffer = buffer;
        this.bufferFromPool = bufferFromPool;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageColorModel getColorModel() {
        return colorModel;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public boolean isBufferFromPool() {
        return bufferFromPool;
    }
}