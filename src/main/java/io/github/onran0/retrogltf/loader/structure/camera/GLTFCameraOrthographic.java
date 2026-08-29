package io.github.onran0.retrogltf.loader.structure.camera;

import org.json.JSONObject;

public class GLTFCameraOrthographic {
    private final float xmag;
    private final float ymag;

    private final float zfar;
    private final float znear;

    public GLTFCameraOrthographic(JSONObject json) {
        this.xmag = json.getFloat("xmag");
        this.ymag = json.getFloat("ymag");

        this.zfar = json.getFloat("zfar");
        this.znear = json.getFloat("znear");
    }

    public float getXMag() {
        return xmag;
    }

    public float getYMag() {
        return ymag;
    }

    public float getZFar() {
        return zfar;
    }

    public float getZNear() {
        return znear;
    }
}