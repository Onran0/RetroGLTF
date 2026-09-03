package io.github.onran0.retrogltf.loader.structure.scene;

import io.github.onran0.retrogltf.loader.util.JSONUtil;

import org.joml.*;
import org.json.*;

import java.nio.FloatBuffer;
import java.util.Optional;

public class GLTFNode {

    private static final Quaternionf TMP_QUAT = new Quaternionf();

    private final String name;
    private final int[] children;

    private final int camera;
    private final int mesh;
    private final int skin;
    private final float[] weights;

    private final Matrix4f matrix;

    public GLTFNode(JSONObject json) {
        this.name = json.optString("name");

        this.children = JSONUtil.toIntArray(json.optJSONArray("children"));

        this.camera = json.optInt("camera", -1);
        this.mesh = json.optInt("mesh", -1);
        this.skin = json.optInt("skin", -1);
        this.weights = JSONUtil.toFloatArray(json.optJSONArray("weights"));

        if(json.has("matrix")) {
            this.matrix = JSONUtil.toMatrix4(json.getJSONArray("matrix"));
        } else {
            JSONArray pos = json.optJSONArray("translation");
            JSONArray rot = json.optJSONArray("rotation");
            JSONArray scl = json.optJSONArray("scale");

            this.matrix = new Matrix4f();

            if(pos != null && rot != null && scl != null) {
                this.matrix.translationRotateScale(
                        pos.getFloat(0), pos.getFloat(1), pos.getFloat(2),
                        rot.getFloat(0), rot.getFloat(1), rot.getFloat(2), rot.getFloat(3),
                        scl.getFloat(0), scl.getFloat(1), scl.getFloat(2)
                );
            } else {
                if(pos != null) {
                    this.matrix.translate(pos.getFloat(0), pos.getFloat(1), pos.getFloat(2));
                }

                if(rot != null) {
                    TMP_QUAT.set(rot.getFloat(0), rot.getFloat(1), rot.getFloat(2), rot.getFloat(3));
                    this.matrix.rotate(TMP_QUAT);
                }

                if(scl != null) {
                    this.matrix.scale(scl.getFloat(0), scl.getFloat(1), scl.getFloat(2));
                }
            }
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
        return Optional.ofNullable(camera == -1 ? null : camera);
    }

    public Optional<Integer> getMesh() {
        return Optional.ofNullable(mesh == -1 ? null : mesh);
    }

    public Optional<Integer> getSkin() {
        return Optional.ofNullable(skin == -1 ? null : skin);
    }

    public Optional<float[]> getWeights() {
        return Optional.ofNullable(weights);
    }

    // other

    public Matrix4f getLocalMatrix() {
        return this.matrix;
    }

    public void getLocalMatrix(FloatBuffer dst) {
        this.matrix.get(dst);
    }
}