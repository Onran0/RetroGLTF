package io.github.onran0.retrogltf.loader.structure.material;

import io.github.onran0.retrogltf.loader.structure.texture.GLTFTextureInfo;
import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.joml.Vector4f;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFPBRMetallicRoughness {
    private final Vector4f baseColorFactor;
    private final GLTFTextureInfo baseColorTexture;
    private final float metallicFactor;
    private final float roughnessFactor;
    private final GLTFTextureInfo metallicRoughnessTexture;

    public GLTFPBRMetallicRoughness(JSONObject json) {
        this.baseColorFactor = JSONUtil.toVector4(json.getJSONArray("baseColorFactor"), new Vector4f(1.0f));

        this.baseColorTexture = JSONUtil.parseNullableObject(json, "baseColorTexture", GLTFTextureInfo::new);
        this.metallicRoughnessTexture = JSONUtil.parseNullableObject(json, "metallicRoughnessTexture", GLTFTextureInfo::new);

        this.metallicFactor = json.optFloat("metallicFactor", 1.0f);
        this.roughnessFactor = json.optFloat("roughnessFactor", 1.0f);
    }

    public Optional<Vector4f> getBaseColorFactor() {
        return Optional.ofNullable(baseColorFactor);
    }

    public Optional<GLTFTextureInfo> getBaseColorTexture() {
        return Optional.ofNullable(baseColorTexture);
    }

    public float getMetallicFactor() {
        return metallicFactor;
    }

    public float getRoughnessFactor() {
        return roughnessFactor;
    }

    public Optional<GLTFTextureInfo> getMetallicRoughnessTexture() {
        return Optional.ofNullable(metallicRoughnessTexture);
    }
}