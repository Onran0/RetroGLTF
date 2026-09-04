package io.github.onran0.retrogltf.enums;

public enum PrimitiveAttributeType {
    POSITION("POSITION", 0),
    NORMAL("NORMAL", 1),
    TEXCOORD_0("TEXCOORD_0", 2),
    TEXCOORD_1("TEXCOORD_1", 3),
    TANGENT("TANGENT", 4),
    JOINTS_0("JOINTS_0",5, true),
    WEIGHTS_0("WEIGHTS_0", 6),
    COLOR_0("COLOR_0", 7);

    private final String id;
    private final int locationPriority;
    private final boolean useIPointer;

    PrimitiveAttributeType(String id, int locationPriority) {
        this(id, locationPriority, false);
    }

    PrimitiveAttributeType(String id, int locationPriority, boolean useIPointer) {
        this.id = id;
        this.locationPriority = locationPriority;
        this.useIPointer = useIPointer;
    }

    public String getId() {
        return id;
    }

    public int getShaderLocationPriority() {
        return locationPriority;
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