package io.github.onran0.retrogltf.loader.structure.animation;

import io.github.onran0.retrogltf.enums.AnimationChannelPath;
import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFAnimationChannel {

    private final int sampler;
    private final Integer targetNode;
    private final AnimationChannelPath targetPath;

    public GLTFAnimationChannel(JSONObject json) {
        this.sampler = json.getInt("sampler");

        JSONObject target = json.getJSONObject("target");

        this.targetNode = JSONUtil.getNullableInt(target, "node");
        this.targetPath = AnimationChannelPath.getById(target.getString("path"));
    }

    public int getSampler() {
        return sampler;
    }

    public Optional<Integer> getTargetNode() {
        return Optional.ofNullable(targetNode);
    }

    public AnimationChannelPath getTargetPath() {
        return targetPath;
    }
}
