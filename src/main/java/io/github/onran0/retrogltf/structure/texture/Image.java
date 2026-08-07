package io.github.onran0.retrogltf.structure.texture;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class Image {
    private final String uri;
    private final String mimeType;
    private final Integer bufferView;

    public Image(JSONObject json) {
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

    public boolean hasBufferView() {
        return bufferView != null;
    }

    public int getBufferView() {
        return bufferView;
    }
}