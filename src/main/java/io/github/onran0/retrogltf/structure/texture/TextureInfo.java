package io.github.onran0.retrogltf.structure.texture;

import org.json.JSONObject;

public class TextureInfo {
    private final int index;
    private final int texCoord;

    public TextureInfo(JSONObject json) {
        this.index = json.getInt("index");
        this.texCoord = json.optInt("texCoord", 0);
    }

    public int getIndex() {
        return index;
    }

    public int getTexCoord() {
        return texCoord;
    }
}