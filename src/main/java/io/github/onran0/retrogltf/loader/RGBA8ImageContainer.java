package io.github.onran0.retrogltf.loader;

import java.nio.ByteBuffer;

class RGBA8ImageContainer {
    private final int width;
    private final int height;
    private final ByteBuffer buffer;
    private final boolean temporaryBuffer;

    public RGBA8ImageContainer(
            final int width,
            final int height,
            final ByteBuffer buffer, final boolean temporaryBuffer
    ) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
        this.temporaryBuffer = temporaryBuffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public boolean isTemporaryBuffer() {
        return temporaryBuffer;
    }
}