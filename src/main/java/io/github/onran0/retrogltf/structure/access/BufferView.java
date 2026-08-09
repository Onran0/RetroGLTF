package io.github.onran0.retrogltf.structure.access;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class BufferView {
    private final int buffer;
    private final int byteLength;
    private final int byteOffset;
    private final Integer byteStride;

    public BufferView(JSONObject json) {
        this.buffer = json.getInt("buffer");
        this.byteLength = json.getInt("byteLength");
        this.byteOffset = json.optInt("byteOffset", 0);
        this.byteStride = JSONUtil.getNullableInt(json, "byteStride");
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

    public boolean hasByteStride() {
        return byteStride != null;
    }

    public int getByteStride() {
        return byteStride;
    }
}