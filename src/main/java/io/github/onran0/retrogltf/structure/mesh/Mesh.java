package io.github.onran0.retrogltf.structure.mesh;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class Mesh {
    private final MeshPrimitive[] primitives;
    private final float[] weights;

    public Mesh(JSONObject json) {
        JSONArray primitivesArray = json.getJSONArray("primitives");

        int primitiveCount = primitivesArray.length();

        this.primitives = new MeshPrimitive[primitiveCount];

        for (int i = 0; i < primitiveCount; i++) {
            this.primitives[i] = new MeshPrimitive(primitivesArray.getJSONObject(i));
        }

        this.weights = JSONUtil.toFloatArray(json.optJSONArray("weights"));
    }

    public MeshPrimitive[] getPrimitives() {
        return primitives;
    }

    public float[] getWeights() { return weights; }
}