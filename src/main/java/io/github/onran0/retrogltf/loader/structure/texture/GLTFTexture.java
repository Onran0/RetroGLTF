package io.github.onran0.retrogltf.loader.structure.texture;

import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFTexture {
    private final Integer sampler;
    private final Integer source;

    public GLTFTexture(JSONObject json) {
        this.sampler = JSONUtil.getNullableInt(json, "sampler");
        this.source = JSONUtil.getNullableInt(json, "source");
    }

    public Optional<Integer> getSampler() {
        return Optional.ofNullable(sampler);
    }

    public Optional<Integer> getSource() {
        return Optional.ofNullable(source);
    }
}