package io.github.onran0.retrogltf.loader.structure.access;

import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFBufferView {
    private final int buffer;
    private final int byteLength;
    private final int byteOffset;
    private final Integer byteStride;

    public GLTFBufferView(JSONObject json) {
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

    public Optional<Integer> getByteStride() {
        return Optional.ofNullable(byteStride);
    }
}