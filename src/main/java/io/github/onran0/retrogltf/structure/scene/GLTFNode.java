package io.github.onran0.retrogltf.structure.scene;

import io.github.onran0.retrogltf.util.JSONUtil;

import org.joml.*;
import org.json.*;

import java.util.Optional;

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

    private final Matrix4f localMatrix;

    public GLTFNode(JSONObject json) {
        this.name = json.optString("name");

        this.children = JSONUtil.toIntArray(json.optJSONArray("children"));

        this.camera = JSONUtil.getNullableInt(json, "camera");
        this.mesh = JSONUtil.getNullableInt(json, "mesh");
        this.skin = JSONUtil.getNullableInt(json, "skin");
        this.weights = JSONUtil.toFloatArray(json.optJSONArray("weights"));

        this.translation = JSONUtil.toVector3(json.getJSONArray("translation"), new Vector3f());
        this.rotation = JSONUtil.toQuaternion(json.getJSONArray("rotation"), new Quaternionf());
        this.scale = JSONUtil.toVector3(json.getJSONArray("translation"), new Vector3f());

        this.matrix = JSONUtil.toMatrix4(json.getJSONArray("matrix"));

        if(this.matrix != null) {
            this.localMatrix = matrix;
        } else {
            this.localMatrix = new Matrix4f();

            this.localMatrix.translate(translation);
            this.localMatrix.rotate(rotation);
            this.localMatrix.scale(scale);
        }
    }

    // default properties getters

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<int[]> getChildren() {
        return Optional.ofNullable(children);
    }

    public Optional<Integer> getCamera() {
        return Optional.ofNullable(camera);
    }

    public Optional<Integer> getMesh() {
        return Optional.ofNullable(mesh);
    }

    public Optional<Integer> getSkin() {
        return Optional.ofNullable(skin);
    }

    public Optional<float[]> getWeights() {
        return Optional.ofNullable(weights);
    }

    public Optional<Vector3f> getTranslation() {
        return Optional.ofNullable(translation);
    }

    public Optional<Quaternionf> getRotation() {
        return Optional.ofNullable(rotation);
    }

    public Optional<Vector3f> getScale() {
        return Optional.ofNullable(scale);
    }

    public Optional<Matrix4f> getDefinedMatrix() {
        return Optional.ofNullable(matrix);
    }

    // other

    public Matrix4f getLocalMatrix() {
        return localMatrix;
    }
}