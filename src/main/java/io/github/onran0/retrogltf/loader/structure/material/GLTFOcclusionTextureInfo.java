package io.github.onran0.retrogltf.loader.structure.material;

import io.github.onran0.retrogltf.loader.structure.texture.GLTFTextureInfo;
import org.json.JSONObject;

public class GLTFOcclusionTextureInfo extends GLTFTextureInfo {

    private final float strength;

    public GLTFOcclusionTextureInfo(JSONObject json) {
        super(json);

        this.strength = json.optFloat("strength", 1.0f);
    }

    public float getStrength() {
        return strength;
    }
}