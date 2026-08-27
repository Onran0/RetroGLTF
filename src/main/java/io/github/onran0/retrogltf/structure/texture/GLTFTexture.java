package io.github.onran0.retrogltf.structure.texture;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFTexture {
    private final int sampler;
    private final Integer source;

    public GLTFTexture(JSONObject json) {
        this.sampler = json.getInt("sampler");
        this.source = JSONUtil.getNullableInt(json, "source");
    }

    public int getSampler() {
        return sampler;
    }

    public Optional<Integer> getSource() {
        return Optional.ofNullable(source);
    }
}