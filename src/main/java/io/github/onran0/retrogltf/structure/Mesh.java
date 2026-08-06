package io.github.onran0.retrogltf.structure;

import org.json.JSONArray;
import org.json.JSONObject;

public class Mesh {
    private final MeshPrimitive[] primitives;

    public Mesh(JSONObject json) {
        JSONArray primitivesArray = json.getJSONArray("primitives");

        int primitiveCount = primitivesArray.length();

        this.primitives = new MeshPrimitive[primitiveCount];

        for (int i = 0; i < primitiveCount; i++) {
            this.primitives[i] = new MeshPrimitive(primitivesArray.getJSONObject(i));
        }
    }

    public MeshPrimitive[] getPrimitives() {
        return primitives;
    }
}