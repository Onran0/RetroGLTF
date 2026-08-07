package io.github.onran0.retrogltf.constants;

public enum TextureWrapMode {
    CLAMP_TO_EDGE(33071),
    MIRRORED_REPEAT(33648),
    REPEAT(10497);

    private final int gltfId;
    private final int glType;

    TextureWrapMode(final int gltfId) {
        this.gltfId = gltfId;
        this.glType = gltfId;
    }

    public int getGLTFId() {
        return gltfId;
    }

    public int getGLType() {
        return glType;
    }

    public static TextureWrapMode getById(int id) {
        for(TextureWrapMode type : TextureWrapMode.values()) {
            if(type.getGLTFId() == id)
                return type;
        }

        return null;
    }
}