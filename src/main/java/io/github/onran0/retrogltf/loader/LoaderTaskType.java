package io.github.onran0.retrogltf.loader;

public enum LoaderTaskType {
    GLTF_READING("gltf_reading"),
    JSON_PARSING("json_parsing"),
    GLTF_PARSING("gltf_parsing"),
    SUB_LOADERS_INVOKER("sub_loaders_invoker"),
    LOAD_CONTEXT_INIT("load_context_init"),
    FILE_BUFFER_READING("file_buffer_reading"),
    BASE64_BUFFER_READING("uri_buffer_reading"),
    MESH_LOADING("mesh_loading"),
    SKIN_LOADING("skin_loading"),
    LIBRARY_BUILTIN_SHADERS_COMPILE("library_builtin_shaders_compile"),
    IMAGE_DECODING("image_decoding"),
    AWT_IMAGE_TO_BUFFER_TRANSCODING("awt_image_to_buffer_transcoding"),
    TEXTURE_LOADING("texture_loading"),
    MATERIALS_LOADING("materials_loading"),
    NODES_LOADING("nodes_loading"),
    DEBUG_0("debug_0"),
    DEBUG_1("debug_1");

    private final String literal;

    LoaderTaskType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}