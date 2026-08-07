package io.github.onran0.retrogltf.structure.scene;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class Scene {
    private final String name;
    private final int[] nodes;

    public Scene(JSONObject json) {
        this.name = json.optString("name");
        this.nodes = JSONUtil.toIntArray(json.optJSONArray("nodes"));
    }

    public String getName() {
        return name;
    }

    public int[] getNodes() {
        return nodes;
    }
}