package io.github.onran0.retrogltf.loader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;

class LoadContext {
    private static final int DIRECT_BUFFERS_COUNT = 2;
    private static final int DIRECT_BUFFER_SIZE = 1024 * 1024 * 4; // 4 MB

    private final ArrayDeque<ByteBuffer> directBuffers = new ArrayDeque<>();

    private BufferViewsReader viewsReader;
    private AccessorsReader accessorsReader;
    private GLTFParser parser;

    public LoadContext() {
        for(int i = 0; i < DIRECT_BUFFERS_COUNT; ++i) {
            directBuffers.add(
                    ByteBuffer.allocateDirect(DIRECT_BUFFER_SIZE)
                            .order(ByteOrder.nativeOrder())
            );
        }
    }

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

    public ByteBuffer popFastBuffer() {
        return this.directBuffers.pop();
    }

    public int getFreeBuffersCount() {
        return this.directBuffers.size();
    }

    public void pushFastBuffer(ByteBuffer buffer) {
        if(!buffer.isDirect())
            throw new IllegalArgumentException("buffer must be direct");

        this.directBuffers.push(buffer);
    }
}