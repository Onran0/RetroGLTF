package io.github.onran0.retrogltf.loader;

import java.nio.ByteBuffer;

class LoadContext {
    private final ByteBuffer fastBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 4); // 4 MB

    private BufferViewsReader viewsReader;
    private AccessorsReader accessorsReader;
    private GLTFParser parser;

    public BufferViewsReader getViewsReader() {
        return viewsReader;
    }

    public void setViewsReader(BufferViewsReader viewsReader) {
        this.viewsReader = viewsReader;
    }

    public AccessorsReader getAccessorsReader() {
        return accessorsReader;
    }

    public void setAccessorsReader(AccessorsReader accessorsReader) {
        this.accessorsReader = accessorsReader;
    }

    public GLTFParser getParser() {
        return parser;
    }

    public void setParser(GLTFParser parser) {
        this.parser = parser;
    }

    public ByteBuffer getFastBuffer() {
        return fastBuffer;
    }
}