package io.github.onran0.retrogltf.structure.texture;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class Texture {
    private final int sampler;
    private final Integer source;

    public Texture(JSONObject json) {
        this.sampler = json.getInt("sampler");
        this.source = JSONUtil.getNullableInt(json, "source");
    }

    public int getSampler() {
        return sampler;
    }

    public boolean hasSource() {
        return source != null;
    }

    public int getSource() {
        return source;
    }
}