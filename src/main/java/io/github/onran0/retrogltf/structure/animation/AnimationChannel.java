package io.github.onran0.retrogltf.structure.animation;

import io.github.onran0.retrogltf.constants.AnimationChannelPath;
import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class AnimationChannel {

    private final int sampler;
    private final Integer targetNode;
    private final AnimationChannelPath targetPath;

    public AnimationChannel(JSONObject json) {
        this.sampler = json.getInt("sampler");

        JSONObject target = json.getJSONObject("target");

        this.targetNode = JSONUtil.getNullableInt(target, "node");
        this.targetPath = AnimationChannelPath.getById(target.getString("path"));
    }

    public int getSampler() {
        return sampler;
    }

    public boolean hasTargetNode() {
        return targetNode != null;
    }

    public int getTargetNode() {
        return targetNode;
    }

    public AnimationChannelPath getTargetPath() {
        return targetPath;
    }
}
