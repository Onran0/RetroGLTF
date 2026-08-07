package io.github.onran0.retrogltf.structure.access;

import org.json.JSONObject;

public class BufferView {
    private final int buffer;
    private final int byteLength;
    private final int byteOffset;
    private final int byteStride;

    public BufferView(JSONObject json) {
        this.buffer = json.getInt("buffer");
        this.byteLength = json.getInt("byteLength");
        this.byteOffset = json.optInt("byteOffset", 0);
        this.byteStride = json.optInt("byteStride", 1);
    }

    // getters

    public int getBuffer() {
        return buffer;
    }

    public int getByteLength() {
        return byteLength;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public int getByteStride() {
        return byteStride;
    }

    // other

    public int getOffsetInBuffer() {
        return byteOffset;
    }
}