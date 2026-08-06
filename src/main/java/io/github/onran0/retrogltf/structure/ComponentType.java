package io.github.onran0.retrogltf.structure;

public enum ComponentType {
    SIGNED_BYTE(5120, 1),
    UNSIGNED_BYTE(5121, 1),
    SIGNED_SHORT(5122, 2),
    UNSIGNED_SHORT(5123, 2),
    UNSIGNED_INT(5125, 4),
    FLOAT(5126, 4);

    private final int id;
    private final int length;

    ComponentType(int id, int length) {
        this.id = id;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public static ComponentType getById(int id) {
        for(ComponentType type : ComponentType.values()) {
            if(type.getId() == id)
                return type;
        }

        return null;
    }
}