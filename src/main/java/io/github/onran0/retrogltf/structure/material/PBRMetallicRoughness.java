package io.github.onran0.retrogltf.structure.material;

import io.github.onran0.retrogltf.structure.texture.TextureInfo;
import io.github.onran0.retrogltf.util.JSONUtil;
import org.joml.Vector4f;
import org.json.JSONObject;

public class PBRMetallicRoughness {
    private final Vector4f baseColorFactor;
    private final TextureInfo baseColorTexture;
    private final float metallicFactor;
    private final float roughnessFactor;
    private final TextureInfo metallicRoughnessTexture;

    public PBRMetallicRoughness(JSONObject json) {
        this.baseColorFactor = JSONUtil.toVector4(json.getJSONArray("baseColorFactor"), new Vector4f(1.0f));

        this.baseColorTexture = JSONUtil.parseNullableObject(json, "baseColorTexture", TextureInfo::new);
        this.metallicRoughnessTexture = JSONUtil.parseNullableObject(json, "metallicRoughnessTexture", TextureInfo::new);

        this.metallicFactor = json.optFloat("metallicFactor", 1.0f);
        this.roughnessFactor = json.optFloat("roughnessFactor", 1.0f);
    }

    public Vector4f getBaseColorFactor() {
        return baseColorFactor;
    }

    public TextureInfo getBaseColorTexture() {
        return baseColorTexture;
    }

    public float getMetallicFactor() {
        return metallicFactor;
    }

    public float getRoughnessFactor() {
        return roughnessFactor;
    }

    public TextureInfo getMetallicRoughnessTexture() {
        return metallicRoughnessTexture;
    }
}