package io.github.onran0.retrogltf.constants;

public enum AnimationChannelPath {
    TRANSLATION("translation"),
    ROTATION("rotation"),
    SCALE("scale"),
    WEIGHTS("weights"),;

    private final String id;

    AnimationChannelPath(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static AnimationChannelPath getById(String id) {
        for(AnimationChannelPath type : AnimationChannelPath.values()) {
            if(type.getId().equals(id))
                return type;
        }

        throw new IllegalArgumentException(id);
    }
}