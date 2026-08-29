package io.github.onran0.retrogltf.loader.structure.material;

import io.github.onran0.retrogltf.enums.TextureAlphaMode;
import io.github.onran0.retrogltf.loader.structure.texture.GLTFTextureInfo;
import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.joml.Vector3f;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFMaterial {

    private final GLTFPBRMetallicRoughness pbrMetallicRoughness;
    private final GLTFNormalTextureInfo normalTexture;
    private final GLTFOcclusionTextureInfo occlusionTexture;
    private final GLTFTextureInfo emissiveTexture;
    private final Vector3f emissiveFactor;
    private final TextureAlphaMode alphaMode;
    private final float alphaCutoff;
    private final boolean doubleSided;

    public GLTFMaterial(JSONObject json) {
        if(json.has("pbrMetallicRoughness")) {
            this.pbrMetallicRoughness = new GLTFPBRMetallicRoughness(json.getJSONObject("pbrMetallicRoughness"));
        } else {
            this.pbrMetallicRoughness = new GLTFPBRMetallicRoughness(new JSONObject());
        }

        this.normalTexture = JSONUtil.parseNullableObject(json, "normalTexture", GLTFNormalTextureInfo::new);
        this.occlusionTexture = JSONUtil.parseNullableObject(json, "occlusionTexture", GLTFOcclusionTextureInfo::new);
        this.emissiveTexture = JSONUtil.parseNullableObject(json, "emissiveTexture", GLTFTextureInfo::new);

        this.emissiveFactor = JSONUtil.toVector3(
                json.getJSONArray("emissiveFactor"),
                new Vector3f(0.0f, 0.0f, 0.0f)
        );

        this.alphaMode = TextureAlphaMode.getById(json.optString("alphaMode", "OPAQUE"));
        this.alphaCutoff = json.optFloat("alphaCutoff", 0.5f);
        this.doubleSided = json.optBoolean("doubleSided", false);
    }

    public GLTFPBRMetallicRoughness getPBRMetallicRoughness() {
        return pbrMetallicRoughness;
    }

    public Optional<GLTFNormalTextureInfo> getNormalTexture() {
        return Optional.ofNullable(normalTexture);
    }

    public Optional<GLTFOcclusionTextureInfo> getOcclusionTexture() {
        return Optional.ofNullable(occlusionTexture);
    }

    public Optional<GLTFTextureInfo> getEmissiveTexture() {
        return Optional.ofNullable(emissiveTexture);
    }

    public Vector3f getEmissiveFactor() {
        return emissiveFactor;
    }

    public TextureAlphaMode getAlphaMode() {
        return alphaMode;
    }

    public float getAlphaCutoff() {
        return alphaCutoff;
    }

    public boolean isDoubleSided() {
        return doubleSided;
    }
}