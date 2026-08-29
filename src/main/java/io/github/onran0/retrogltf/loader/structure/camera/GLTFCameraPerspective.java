package io.github.onran0.retrogltf.loader.structure.camera;

import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFCameraPerspective {
    private final Float aspectRatio;
    private final float yfov;
    private final Float zfar;
    private final float znear;

    public GLTFCameraPerspective(JSONObject json) {
        this.aspectRatio = JSONUtil.getNullableFloat(json, "aspectRatio");
        this.yfov = json.getFloat("yfov");
        this.zfar = JSONUtil.getNullableFloat(json, "zfar");
        this.znear = json.getFloat("znear");
    }

    public Optional<Float> getAspectRatio() {
        return Optional.ofNullable(aspectRatio);
    }

    public float getYFov() {
        return yfov;
    }

    public Optional<Float> getZFar() {
        return Optional.ofNullable(zfar);
    }

    public float getZNear() {
        return znear;
    }
}