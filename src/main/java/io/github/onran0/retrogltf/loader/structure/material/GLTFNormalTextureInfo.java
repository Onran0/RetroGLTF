package io.github.onran0.retrogltf.loader.structure.material;

import io.github.onran0.retrogltf.loader.structure.texture.GLTFTextureInfo;
import org.json.JSONObject;

public class GLTFNormalTextureInfo extends GLTFTextureInfo {

    private final float scale;

    public GLTFNormalTextureInfo(JSONObject json) {
        super(json);

        this.scale = json.optFloat("scale", 1.0f);
    }

    public float getScale() {
        return scale;
    }
}