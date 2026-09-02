package io.github.onran0.retrogltf.loader;

public enum LoaderTaskType {
    GLTF_READING("gltf_reading"),
    JSON_PARSING("json_parsing"),
    GLTF_PARSING("gltf_parsing"),
    FILE_BUFFER_READING("file_buffer_reading"),
    BASE64_BUFFER_READING("uri_buffer_reading"),
    MESH_LOADING("mesh_loading"),
    IMAGE_DECODING("image_decoding"),
    AWT_IMAGE_TO_BUFFER_TRANSCODING("awt_image_to_buffer_transcoding"),
    TEXTURE_LOADING("texture_loading"),
    MATERIALS_LOADING("materials_loading"),
    NODES_LOADING("nodes_loading"),;

    private final String literal;

    LoaderTaskType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}