package io.github.onran0.retrogltf.structure.material;

import io.github.onran0.retrogltf.constants.TextureAlphaMode;
import io.github.onran0.retrogltf.structure.texture.TextureInfo;
import io.github.onran0.retrogltf.util.JSONUtil;
import org.joml.Vector3f;
import org.json.JSONObject;

public class Material {

    private final PBRMetallicRoughness pbrMetallicRoughness;
    private final NormalTextureInfo normalTexture;
    private final OcclusionTextureInfo occlusionTexture;
    private final TextureInfo emissiveTexture;
    private final Vector3f emissiveFactor;
    private final TextureAlphaMode alphaMode;
    private final float alphaCutoff;
    private final boolean doubleSided;

    public Material(JSONObject json) {
        if(json.has("pbrMetallicRoughness")) {
            this.pbrMetallicRoughness = new PBRMetallicRoughness(json.getJSONObject("pbrMetallicRoughness"));
        } else {
            this.pbrMetallicRoughness = new PBRMetallicRoughness(new JSONObject());
        }

        this.normalTexture = JSONUtil.parseNullableObject(json, "normalTexture", NormalTextureInfo::new);
        this.occlusionTexture = JSONUtil.parseNullableObject(json, "occlusionTexture", OcclusionTextureInfo::new);
        this.emissiveTexture = JSONUtil.parseNullableObject(json, "emissiveTexture", TextureInfo::new);

        this.emissiveFactor = JSONUtil.toVector3(
                json.getJSONArray("emissiveFactor"),
                new Vector3f(0.0f, 0.0f, 0.0f)
        );

        this.alphaMode = TextureAlphaMode.getById(json.optString("alphaMode", "OPAQUE"));
        this.alphaCutoff = json.optFloat("alphaCutoff", 0.5f);
        this.doubleSided = json.optBoolean("doubleSided", false);
    }

    public PBRMetallicRoughness getPBRMetallicRoughness() {
        return pbrMetallicRoughness;
    }

    public NormalTextureInfo getNormalTexture() {
        return normalTexture;
    }

    public OcclusionTextureInfo getOcclusionTexture() {
        return occlusionTexture;
    }

    public TextureInfo getEmissiveTexture() {
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