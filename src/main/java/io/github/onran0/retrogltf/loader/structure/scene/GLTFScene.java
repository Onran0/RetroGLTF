package io.github.onran0.retrogltf.loader.structure.scene;

import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFScene {
    private final String name;
    private final int[] nodes;

    public GLTFScene(JSONObject json) {
        this.name = json.optString("name");
        this.nodes = JSONUtil.toIntArray(json.optJSONArray("nodes"));
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<int[]> getNodes() {
        return Optional.ofNullable(nodes);
    }
}