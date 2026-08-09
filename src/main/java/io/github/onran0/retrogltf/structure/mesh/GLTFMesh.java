package io.github.onran0.retrogltf.structure.mesh;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class GLTFMesh {
    private final GLTFMeshPrimitive[] primitives;
    private final float[] weights;

    public GLTFMesh(JSONObject json) {
        JSONArray primitivesArray = json.getJSONArray("primitives");

        int primitiveCount = primitivesArray.length();

        this.primitives = new GLTFMeshPrimitive[primitiveCount];

        for (int i = 0; i < primitiveCount; i++) {
            this.primitives[i] = new GLTFMeshPrimitive(primitivesArray.getJSONObject(i));
        }

        this.weights = JSONUtil.toFloatArray(json.optJSONArray("weights"));
    }

    public GLTFMeshPrimitive[] getPrimitives() {
        return primitives;
    }

    public float[] getWeights() { return weights; }
}