package io.github.onran0.retrogltf.structure;

import org.json.JSONArray;
import org.json.JSONObject;

public class Accessor {
    private final int bufferView;
    private final int byteOffset;

    private final ComponentType componentType;
    private final int count;

    private final float[] min;
    private final float[] max;

    private final AccessorType type;
    private final boolean normalized;

    private final SparseAccessor sparse;

    public Accessor(JSONObject json) {
        this.bufferView = json.getInt("bufferView");
        this.byteOffset = json.optInt("byteOffset", 0);

        this.componentType = ComponentType.getById(json.getInt("componentType"));
        this.count = json.getInt("count");

        JSONArray minJson = json.optJSONArray("min");

        if(minJson != null) {
            JSONArray maxJson = json.getJSONArray("max");

            this.min = new float[minJson.length()];
            this.max = new float[maxJson.length()];

            for(int i = 0;i < this.min.length;i++) {
                this.min[i] = minJson.getFloat(i);
                this.max[i] = maxJson.getFloat(i);
            }
        } else {
            this.min = null;
            this.max = null;
        }

        this.type = AccessorType.getById(json.getString("type"));
        this.normalized = json.optBoolean("normalized", false);

        JSONObject sparse = json.optJSONObject("sparse");

        if(sparse != null) {
            this.sparse = new SparseAccessor(sparse);
        } else {
            this.sparse = null;
        }
    }

    // getters

    public int getBufferView() {
        return bufferView;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public int getCount() {
        return count;
    }

    public float[] getMin() {
        return min;
    }

    public float[] getMax() {
        return max;
    }

    public AccessorType getType() {
        return type;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public SparseAccessor getSparse() {
        return sparse;
    }

    // other

    public int getLength(int byteStride) {
        return byteStride * (count - 1) + componentType.getLength() * type.getNumberOfComponents();
    }
}