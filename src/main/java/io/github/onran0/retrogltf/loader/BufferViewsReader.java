package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.loader.structure.access.GLTFBufferView;

import java.nio.ByteBuffer;

class BufferViewsReader {
    private final ByteBuffer[] buffers;
    private final GLTFBufferView[] views;

    public BufferViewsReader(ByteBuffer[] buffers, GLTFBufferView[] views) {
        this.buffers = buffers;
        this.views = views;
    }

    public int getViewLength(int id) {
        return views[id].getByteLength();
    }

    public void get(ByteBuffer buf, int id, int offset) {
        get(buf, id, offset, views[id].getByteLength());
    }

    public void get(ByteBuffer buf, int id, int offset, int length) {
        GLTFBufferView view = views[id];
        ByteBuffer src = buffers[view.getBuffer()];

        int oldPos = src.position();
        int oldLimit = src.limit();
        int oldDstPos = buf.position();

        int pos = view.getByteOffset() + offset;

        src.position(pos);
        src.limit(pos + length);

        buf.put(src);

        src.position(oldPos);
        src.limit(oldLimit);
        buf.position(oldDstPos);
    }
}