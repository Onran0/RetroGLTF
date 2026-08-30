package io.github.onran0.retrogltf.loader.structure.access;

import io.github.onran0.retrogltf.enums.ComponentType;
import io.github.onran0.retrogltf.loader.util.JSONUtil;
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

        int componentSize = componentType.getLength();

        if(!this.type.isMatrixType()) {
            this.elementSize = componentSize * type.getNumberOfComponents();
        } else {
            int dim = type.getMatrixDimension();

            int columnPaddingBytes = (componentSize * dim) % 4;
            int columnLengthInBytes = componentSize * dim + columnPaddingBytes;

            this.elementSize = columnLengthInBytes * dim;
        }
    }

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

    public int getElementSize() {
        return elementSize;
    }
}