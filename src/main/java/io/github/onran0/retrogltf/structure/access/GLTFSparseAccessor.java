package io.github.onran0.retrogltf.structure.access;

import io.github.onran0.retrogltf.constants.ComponentType;
import org.json.JSONObject;

public class GLTFSparseAccessor {
    private final int count;

    private final int indicesBufferView;
    private final int indicesByteOffset;
    private final ComponentType indicesComponentType;

    private final int valuesBufferView;
    private final int valuesByteOffset;

    public GLTFSparseAccessor(JSONObject json) {
        this.count = json.getInt("count");

        JSONObject indices = json.getJSONObject("indices");

        this.indicesBufferView = indices.getInt("bufferView");
        this.indicesByteOffset = indices.optInt("byteOffset", 0);
        this.indicesComponentType = ComponentType.getById(indices.getInt("componentType"));

        JSONObject values = json.getJSONObject("values");

        this.valuesBufferView = values.getInt("bufferView");
        this.valuesByteOffset = values.optInt("byteOffset", 0);
    }

    public int getCount() {
        return count;
    }

    public int getIndicesBufferView() {
        return indicesBufferView;
    }

    public int getIndicesByteOffset() {
        return indicesByteOffset;
    }

    public ComponentType getIndicesComponentType() {
        return indicesComponentType;
    }

    public int getValuesBufferView() {
        return valuesBufferView;
    }

    public int getValuesByteOffset() {
        return valuesByteOffset;
    }
}