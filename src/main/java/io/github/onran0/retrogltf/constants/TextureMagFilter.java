package io.github.onran0.retrogltf.constants;

public enum TextureMagFilter {
    NEAREST(9728),
    LINEAR(9729);

    private final int gltfId;
    private final int glType;

    TextureMagFilter(final int gltfId) {
        this.gltfId = gltfId;
        this.glType = gltfId;
    }

    public int getGLTFId() {
        return gltfId;
    }

    public int getGLType() {
        return glType;
    }

    public static TextureMagFilter getById(int id) {
        for(TextureMagFilter type : TextureMagFilter.values()) {
            if(type.getGLTFId() == id)
                return type;
        }

        throw new IllegalArgumentException(String.valueOf(id));
    }
}