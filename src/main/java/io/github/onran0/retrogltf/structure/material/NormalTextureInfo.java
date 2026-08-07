package io.github.onran0.retrogltf.structure.material;

import io.github.onran0.retrogltf.structure.texture.TextureInfo;
import org.json.JSONObject;

public class NormalTextureInfo extends TextureInfo {

    private final float scale;

    public NormalTextureInfo(JSONObject json) {
        super(json);

        this.scale = json.optFloat("scale", 1.0f);
    }

    public float getScale() {
        return scale;
    }
}