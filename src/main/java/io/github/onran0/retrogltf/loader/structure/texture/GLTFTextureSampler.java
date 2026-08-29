package io.github.onran0.retrogltf.loader.structure.texture;

import io.github.onran0.retrogltf.enums.TextureMagFilter;
import io.github.onran0.retrogltf.enums.TextureMinFilter;
import io.github.onran0.retrogltf.enums.TextureWrapMode;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFTextureSampler {
    private final TextureMagFilter magFilter;
    private final TextureMinFilter minFilter;

    private final TextureWrapMode wrapS;
    private final TextureWrapMode wrapT;

    public GLTFTextureSampler(JSONObject json) {
        if(json.has("magFilter")) {
            this.magFilter = TextureMagFilter.getById(json.getInt("magFilter"));
        } else {
            this.magFilter = null;
        }

        if(json.has("minFilter")) {
            this.minFilter = TextureMinFilter.getById(json.getInt("minFilter"));
        } else {
            this.minFilter = null;
        }

        this.wrapS = TextureWrapMode.getById(json.optInt("wrapS", 10497));
        this.wrapT = TextureWrapMode.getById(json.optInt("wrapT", 10497));
    }

    public Optional<TextureMagFilter> getMagFilter() {
        return Optional.ofNullable(magFilter);
    }

    public Optional<TextureMinFilter> getMinFilter() {
        return Optional.ofNullable(minFilter);
    }

    public TextureWrapMode getWrapS() {
        return wrapS;
    }

    public TextureWrapMode getWrapT() {
        return wrapT;
    }
}