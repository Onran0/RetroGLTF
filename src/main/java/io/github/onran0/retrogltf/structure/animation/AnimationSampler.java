package io.github.onran0.retrogltf.structure.animation;

import io.github.onran0.retrogltf.constants.AnimationInterpolationType;

import org.json.JSONObject;

public class AnimationSampler {
    private final int input;
    private final AnimationInterpolationType interpolation;
    private final int output;

    public AnimationSampler(JSONObject json) {
        this.input = json.getInt("input");
        this.interpolation = AnimationInterpolationType.getById(json.optString("interpolation", "LINEAR"));
        this.output = json.getInt("output");
    }

    public int getInput() {
        return input;
    }

    public AnimationInterpolationType getInterpolation() {
        return interpolation;
    }

    public int getOutput() {
        return output;
    }
}