package io.github.onran0.retrogltf;

import io.github.onran0.retrogltf.io.IFileProvider;
import io.github.onran0.retrogltf.io.LegacyFileProvider;
import io.github.onran0.retrogltf.io.NIOFileProvider;
import org.json.JSONObject;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public class GLTFParser {

    private final JSONObject root;
    private final IFileProvider fileProvider;
    private final ByteBuffer glbBuffer;

    public GLTFParser(JSONObject root) {
        this.root = root;
        this.fileProvider = null;
        this.glbBuffer = null;
    }

    public GLTFParser(JSONObject root, ByteBuffer glbBuffer) {
        this.root = root;
        this.fileProvider = null;
        this.glbBuffer = glbBuffer;
    }

    public GLTFParser(JSONObject root, IFileProvider fileProvider) {
        this.root = root;
        this.fileProvider = fileProvider;
        this.glbBuffer = null;
    }

    public GLTFParser(JSONObject root, File dir) {
        this(root, new LegacyFileProvider(dir));
    }

    public GLTFParser(JSONObject root, Path dir) {
        this(root, new NIOFileProvider(dir));
    }

    public void parse() {
        for(Object buffer : root.getJSONArray("buffers")) {
            JSONObject bufferJson = (JSONObject) buffer;


        }
    }
}