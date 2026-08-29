package io.github.onran0.retrogltf.enums;

public enum ComponentType {
    SIGNED_BYTE(5120, 1),
    UNSIGNED_BYTE(5121, 1),
    SIGNED_SHORT(5122, 2),
    UNSIGNED_SHORT(5123, 2),
    UNSIGNED_INT(5125, 4),
    FLOAT(5126, 4);

    private final int gltfId;
    private final int glType;
    private final int length;

    ComponentType(int gltfId, int length) {
        this.gltfId = gltfId;
        this.glType = gltfId;
        this.length = length;
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

    public static ComponentType getById(int id) {
        for(ComponentType type : ComponentType.values()) {
            if(type.getGLTFId() == id)
                return type;
        }

        throw new IllegalArgumentException(String.valueOf(id));
    }
}