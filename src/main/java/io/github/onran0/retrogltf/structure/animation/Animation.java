package io.github.onran0.retrogltf.structure.animation;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class Animation {
    private final String name;
    private final AnimationChannel[] channels;
    private final AnimationSampler[] samplers;

    public Animation(JSONObject json) {
        this.name = json.optString("name");

        this.channels = JSONUtil.toObjectArray(
                json.getJSONArray("channels"),
                AnimationChannel[]::new,
                AnimationChannel::new
        );

        this.samplers = JSONUtil.toObjectArray(
                json.getJSONArray("samplers"),
                AnimationSampler[]::new,
                AnimationSampler::new
        );
    }

    public String getName() {
        return name;
    }

    public AnimationChannel[] getChannels() {
        return channels;
    }

    public AnimationSampler[] getSamplers() {
        return samplers;
    }
}