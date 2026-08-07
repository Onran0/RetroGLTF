package io.github.onran0.retrogltf.constants;

public enum CameraType {
    PERSPECTIVE("perspective"),
    ORTHOGRAPHIC("orthographic"),;

    private final String id;

    CameraType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static CameraType getById(String id) {
        for(CameraType type : CameraType.values()) {
            if(type.getId().equals(id))
                return type;
        }

        return null;
    }
}