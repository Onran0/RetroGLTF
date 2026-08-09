package io.github.onran0.retrogltf.structure.material;

import io.github.onran0.retrogltf.constants.TextureAlphaMode;
import io.github.onran0.retrogltf.structure.texture.GLTFTextureInfo;
import io.github.onran0.retrogltf.util.JSONUtil;
import org.joml.Vector3f;
import org.json.JSONObject;

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

    public GLTFNormalTextureInfo getNormalTexture() {
        return normalTexture;
    }

    public GLTFOcclusionTextureInfo getOcclusionTexture() {
        return occlusionTexture;
    }

    public GLTFTextureInfo getEmissiveTexture() {
        return emissiveTexture;
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