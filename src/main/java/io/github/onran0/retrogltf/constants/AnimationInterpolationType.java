package io.github.onran0.retrogltf.constants;

public enum AnimationInterpolationType {
    STEP("STEP"),
    LINEAR("LINEAR"),
    CUBICSPLINE("CUBICSPLINE");

    private final String id;

    AnimationInterpolationType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static AnimationInterpolationType getById(String id) {
        for(AnimationInterpolationType type : AnimationInterpolationType.values()) {
            if(type.getId().equals(id))
                return type;
        }

        return null;
    }
}