package io.github.onran0.retrogltf.enums;

public enum PrimitiveAttributeTypes {
    POSITION("POSITION", 0),
    NORMAL("NORMAL", 1),
    TANGENT("TANGENT", 6),
    COLOR_0("COLOR_0", 7),
    TEXCOORD_0("TEXCOORD_0", 2),
    TEXCOORD_1("TEXCOORD_1", 3),
    JOINTS_0("JOINTS_0",4),
    WEIGHTS_0("WEIGHTS_0", 5);

    private final String id;
    private final int location;

    PrimitiveAttributeTypes(String id, int location) {
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public int getShaderLocation() {
        return location;
    }

    public static PrimitiveAttributeTypes getById(String id) {
        for(PrimitiveAttributeTypes type : PrimitiveAttributeTypes.values()) {
            if(type.getId().equals(id))
                return type;
        }

        return null;
    }
}