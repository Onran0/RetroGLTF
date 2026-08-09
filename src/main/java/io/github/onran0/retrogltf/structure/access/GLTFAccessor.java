package io.github.onran0.retrogltf.structure.access;

import io.github.onran0.retrogltf.constants.ComponentType;
import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class GLTFAccessor {
    private final Integer bufferView;
    private final int byteOffset;

    private final ComponentType componentType;
    private final int count;

    private final float[] min;
    private final float[] max;

    private final GLTFAccessorType type;
    private final boolean normalized;

    private final GLTFSparseAccessor sparse;
    
    private final int elementSize;

    public GLTFAccessor(JSONObject json) {
        this.bufferView = JSONUtil.getNullableInt(json, "bufferView");
        this.byteOffset = json.optInt("byteOffset", 0);

        this.componentType = ComponentType.getById(json.getInt("componentType"));
        this.count = json.getInt("count");

        this.min = JSONUtil.toFloatArray(json.optJSONArray("min"));
        this.max = JSONUtil.toFloatArray(json.optJSONArray("max"));

        this.type = GLTFAccessorType.getById(json.getString("type"));
        this.normalized = json.optBoolean("normalized", false);

        JSONObject sparse = json.optJSONObject("sparse");

        if(sparse != null) {
            this.sparse = new GLTFSparseAccessor(sparse);
        } else {
            this.sparse = null;
        }

        this.elementSize = componentType.getLength() * type.getNumberOfComponents();
    }

    // getters

    public boolean hasBufferView() {
        return bufferView != null;
    }

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

    public GLTFAccessorType getType() {
        return type;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public GLTFSparseAccessor getSparse() {
        return sparse;
    }

    // other

    public int getElementSize() {
        return this.elementSize;
    }

    public int getEffectiveByteStride(GLTFBufferView view) {
        return view.hasByteStride() ? view.getByteStride() : this.elementSize;
    }

    public int getLength(GLTFBufferView view) {
        return getLength(getEffectiveByteStride(view));
    }

    public int getLength(int byteStride) {
        return byteStride * (count - 1) + this.elementSize;
    }

    public int getElementIndexInBuffer(GLTFBufferView view, int index) {
        return getElementIndexInBuffer(index, getEffectiveByteStride(view), view.getByteOffset());
    }

    public int getElementIndexInBuffer(int index, int byteStride, int offset) {
        return index * byteStride + byteOffset + offset;
    }
}