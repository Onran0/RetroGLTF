package io.github.onran0.retrogltf.structure.access;

import org.json.JSONObject;

import java.util.Optional;

public class GLTFBuffer {
    private final int byteLength;
    private final String uri;

    public GLTFBuffer(JSONObject json) {
        this.byteLength = json.getInt("byteLength");
        this.uri = json.optString("uri");
    }

    public int getByteLength() {
        return byteLength;
    }

    public Optional<String> getURI() {
        return Optional.ofNullable(uri);
    }
}