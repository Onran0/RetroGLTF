package io.github.onran0.retrogltf.loader.structure.animation;

import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.json.JSONObject;

public class GLTFAnimation {
    private final String name;
    private final GLTFAnimationChannel[] channels;
    private final GLTFAnimationSampler[] samplers;

    public GLTFAnimation(JSONObject json) {
        this.name = json.optString("name");

        this.channels = JSONUtil.toObjectArray(
                json.getJSONArray("channels"),
                GLTFAnimationChannel[]::new,
                GLTFAnimationChannel::new
        );

        this.samplers = JSONUtil.toObjectArray(
                json.getJSONArray("samplers"),
                GLTFAnimationSampler[]::new,
                GLTFAnimationSampler::new
        );
    }

    public String getName() {
        return name;
    }

    public GLTFAnimationChannel[] getChannels() {
        return channels;
    }

    public GLTFAnimationSampler[] getSamplers() {
        return samplers;
    }
}