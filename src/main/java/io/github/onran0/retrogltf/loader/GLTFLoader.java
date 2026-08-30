package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.loader.io.IFileProvider;
import io.github.onran0.retrogltf.loader.io.LegacyFileProvider;
import io.github.onran0.retrogltf.loader.io.NIOFileProvider;
import org.json.JSONObject;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public class GLTFLoader {
    private final GLTFParser parser;

    public GLTFLoader(JSONObject root) {
        this.parser = new GLTFParser(root);
    }

    public GLTFLoader(JSONObject root, ByteBuffer glbBuffer) {
        this.parser = new GLTFParser(root, glbBuffer);
    }

    public GLTFLoader(JSONObject root, IFileProvider fileProvider) {
        this.parser = new GLTFParser(root, fileProvider);
    }

    public GLTFLoader(JSONObject root, File dir) {
        this.parser = new GLTFParser(root, dir);
    }

    public GLTFLoader(JSONObject root, Path dir) {
        this.parser = new GLTFParser(root, dir);
    }

    public void load() throws GLTFLoadException {
        this.parser.parse();

        BufferViewsReader viewsReader = new BufferViewsReader(
                this.parser.getBuffers(),
                this.parser.getViews()
        );

        AccessorsReader accessorsReader = new AccessorsReader(
                this.parser.getAccessors(),
                this.parser.getViews(),
                viewsReader
        );
    }
}