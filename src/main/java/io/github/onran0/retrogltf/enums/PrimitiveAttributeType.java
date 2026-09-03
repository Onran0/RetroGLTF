package io.github.onran0.retrogltf.enums;

public enum PrimitiveAttributeType {
    POSITION("POSITION", 0),
    NORMAL("NORMAL", 1),
    TANGENT("TANGENT", 6),
    COLOR_0("COLOR_0", 7),
    TEXCOORD_0("TEXCOORD_0", 2),
    TEXCOORD_1("TEXCOORD_1", 3),
    JOINTS_0("JOINTS_0",4, true),
    WEIGHTS_0("WEIGHTS_0", 5);

    private final String id;
    private final int location;
    private final boolean useIPointer;

    PrimitiveAttributeType(String id, int location) {
        this(id, location, false);
    }

    PrimitiveAttributeType(String id, int location, boolean useIPointer) {
        this.id = id;
        this.location = location;
        this.useIPointer = useIPointer;
    }

    public String getId() {
        return id;
    }

    public int getShaderLocation() {
        return location;
    }

    public boolean shouldUseIPointerForVAO() {
        return useIPointer;
    }

    public static PrimitiveAttributeType getById(String id) {
        for(PrimitiveAttributeType type : PrimitiveAttributeType.values()) {
            if(type.getId().equals(id))
                return type;
        }

        return null;
    }
}