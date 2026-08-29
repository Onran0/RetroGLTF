package io.github.onran0.retrogltf.structure.texture;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFImage {
    private final String uri;
    private final String mimeType;
    private final Integer bufferView;

    public GLTFImage(JSONObject json) {
        this.uri = json.optString("uri", null);
        this.mimeType = json.optString("mimeType", null);
        this.bufferView = JSONUtil.getNullableInt(json, "bufferView");
    }

    public Optional<String> getURI() {
        return Optional.ofNullable(uri);
    }

    public Optional<String> getMimeType() {
        return Optional.ofNullable(mimeType);
    }

    public Optional<Integer> getBufferView() {
        return Optional.ofNullable(bufferView);
    }
}