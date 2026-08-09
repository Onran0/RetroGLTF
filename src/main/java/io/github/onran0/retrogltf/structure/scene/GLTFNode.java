package io.github.onran0.retrogltf.structure.scene;

import io.github.onran0.retrogltf.util.JSONUtil;

import org.joml.*;
import org.json.*;

public class GLTFNode {

    private final String name;
    private final int[] children;

    private final Integer camera;
    private final Integer mesh;
    private final Integer skin;
    private final float[] weights;

    private final Vector3f translation;
    private final Quaternionf rotation;
    private final Vector3f scale;
    private final Matrix4f matrix;

    public GLTFNode(JSONObject json) {
        this.name = json.optString("name");

        this.children = JSONUtil.toIntArray(json.optJSONArray("children"));

        this.camera = JSONUtil.getNullableInt(json, "camera");
        this.mesh = JSONUtil.getNullableInt(json, "mesh");
        this.skin = JSONUtil.getNullableInt(json, "skin");
        this.weights = JSONUtil.toFloatArray(json.optJSONArray("weights"));

        this.translation = JSONUtil.toVector3(json.getJSONArray("translation"));
        this.rotation = JSONUtil.toQuaternion(json.getJSONArray("rotation"));
        this.scale = JSONUtil.toVector3(json.getJSONArray("translation"));

        this.matrix = JSONUtil.toMatrix4(json.getJSONArray("matrix"));
    }

    public String getName() {
        return name;
    }

    public int[] getChildren() {
        return children;
    }

    public boolean hasCamera() {
        return camera != null;
    }

    public int getCamera() {
        return camera;
    }

    public boolean hasMesh() {
        return mesh != null;
    }

    public int getMesh() {
        return mesh;
    }

    public boolean hasSkin() {
        return skin != null;
    }

    public int getSkin() {
        return skin;
    }

    public float[] getWeights() {
        return weights;
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Matrix4f getMatrix() {
        return matrix;
    }
}