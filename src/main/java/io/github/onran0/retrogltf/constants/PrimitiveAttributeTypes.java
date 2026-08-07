package io.github.onran0.retrogltf.constants;

public enum PrimitiveAttributeTypes {
    POSITION("POSITION"),
    NORMAL("NORMAL"),
    TANGENT("TANGENT"),
    COLOR_0("COLOR_0"),
    TEXCOORD_0("TEXCOORD_0"),
    TEXCOORD_1("TEXCOORD_1"),
    JOINTS_0("JOINTS_0"),
    WEIGHTS_0("WEIGHTS_0");

    private final String id;

    PrimitiveAttributeTypes(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static PrimitiveAttributeTypes getById(String id) {
        for(PrimitiveAttributeTypes type : PrimitiveAttributeTypes.values()) {
            if(type.getId().equals(id))
                return type;
        }

        return null;
    }
}