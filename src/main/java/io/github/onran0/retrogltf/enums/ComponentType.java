package io.github.onran0.retrogltf.enums;

import java.util.Optional;

public enum ComponentType {
    SIGNED_BYTE(5120, 1, 127L),
    UNSIGNED_BYTE(5121, 1, 255L),
    SIGNED_SHORT(5122, 2, 32767L),
    UNSIGNED_SHORT(5123, 2, 65535L),
    UNSIGNED_INT(5125, 4, 4294967295L),
    FLOAT(5126, 4, 0L);

    private final int gltfId;
    private final int glType;
    private final int length;
    private final long maxValue;

    ComponentType(int gltfId, int length, long maxValue) {
        this.gltfId = gltfId;
        this.glType = gltfId;
        this.length = length;
        this.maxValue = maxValue;
    }

    public int getGLTFId() {
        return gltfId;
    }

    public int getGLType() {
        return glType;
    }

    public int getLength() {
        return length;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public static ComponentType getById(int id) {
        for(ComponentType type : ComponentType.values()) {
            if(type.getGLTFId() == id)
                return type;
        }

        throw new IllegalArgumentException(String.valueOf(id));
    }
}