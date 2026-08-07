package io.github.onran0.retrogltf.structure.camera;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class CameraPerspective {
    private final Float aspectRatio;
    private final float yfov;
    private final Float zfar;
    private final float znear;

    public CameraPerspective(JSONObject json) {
        this.aspectRatio = JSONUtil.getNullableFloat(json, "aspectRatio");
        this.yfov = json.getFloat("yfov");
        this.zfar = JSONUtil.getNullableFloat(json, "zfar");
        this.znear = json.getFloat("znear");
    }

    public boolean hasAspectRatio() {
        return aspectRatio != null;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public float getYFov() {
        return yfov;
    }

    public boolean hasZFar() {
        return zfar != null;
    }

    public float getZFar() {
        return zfar;
    }

    public float getZNear() {
        return znear;
    }
}