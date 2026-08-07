package io.github.onran0.retrogltf.structure.material;

import io.github.onran0.retrogltf.structure.texture.TextureInfo;
import org.json.JSONObject;

public class OcclusionTextureInfo extends TextureInfo {

    private final float strength;

    public OcclusionTextureInfo(JSONObject json) {
        super(json);

        this.strength = json.optFloat("strength", 1.0f);
    }

    public float getStrength() {
        return strength;
    }
}