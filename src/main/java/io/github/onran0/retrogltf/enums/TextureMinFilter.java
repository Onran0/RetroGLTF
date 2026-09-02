package io.github.onran0.retrogltf.enums;

public enum TextureMinFilter {
    NEAREST(9728, false),
    LINEAR(9729, false),
    NEAREST_MIPMAP_NEAREST(9984, true),
    LINEAR_MIPMAP_NEAREST(9985, true),
    NEAREST_MIPMAP_LINEAR(9986, true),
    LINEAR_MIPMAP_LINEAR(9987, true);

    private final int gltfId;
    private final int glType;

    private final boolean mipmap;

    TextureMinFilter(final int gltfId, final boolean mipmap) {
        this.gltfId = gltfId;
        this.glType = gltfId;
        this.mipmap = mipmap;
    }

    public int getGLTFId() {
        return gltfId;
    }

    public int getGLType() {
        return glType;
    }

    public boolean isMipmap() {
        return mipmap;
    }

    public static TextureMinFilter getById(int id) {
        for(TextureMinFilter type : TextureMinFilter.values()) {
            if(type.getGLTFId() == id)
                return type;
        }

        throw new IllegalArgumentException(String.valueOf(id));
    }
}