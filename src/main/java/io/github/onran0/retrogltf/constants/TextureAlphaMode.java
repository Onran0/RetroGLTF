package io.github.onran0.retrogltf.constants;

public enum TextureAlphaMode {
    OPAQUE("OPAQUE"),
    MASK("MASK"),
    BLEND("BLEND");

    private final String id;

    TextureAlphaMode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static TextureAlphaMode getById(String id) {
        for(TextureAlphaMode type : TextureAlphaMode.values()) {
            if(type.getId().equals(id))
                return type;
        }

        return null;
    }
}