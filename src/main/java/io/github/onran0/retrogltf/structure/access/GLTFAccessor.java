package io.github.onran0.retrogltf.structure.access;

import io.github.onran0.retrogltf.constants.ComponentType;
import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

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

        if(json.has("sparse")) {
            this.sparse = new GLTFSparseAccessor(json.getJSONObject("sparse"));
        } else {
            this.sparse = null;
        }

        this.elementSize = componentType.getLength() * type.getNumberOfComponents();
    }

    // getters

    public Optional<Integer> getBufferView() {
        return Optional.ofNullable(bufferView);
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

    public Optional<float[]> getMin() {
        return Optional.ofNullable(min);
    }

    public Optional<float[]> getMax() {
        return Optional.ofNullable(max);
    }

    public GLTFAccessorType getType() {
        return type;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public Optional<GLTFSparseAccessor> getSparse() {
        return Optional.ofNullable(sparse);
    }

    // other

    public int getElementSize() {
        return this.elementSize;
    }

    public int getEffectiveByteStride(GLTFBufferView view) {
        return view.getByteStride().orElse(this.elementSize);
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