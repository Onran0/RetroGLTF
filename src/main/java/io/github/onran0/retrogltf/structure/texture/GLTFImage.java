package io.github.onran0.retrogltf.structure.texture;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFImage {
    private final String uri;
    private final String mimeType;
    private final Integer bufferView;

    public GLTFImage(JSONObject json) {
        this.uri = json.getString("uri");
        this.mimeType = json.getString("mimeType");
        this.bufferView = JSONUtil.getNullableInt(json, "bufferView");
    }

    public String getURI() {
        return uri;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Optional<Integer> getBufferView() {
        return Optional.ofNullable(bufferView);
    }
}