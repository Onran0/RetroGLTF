package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLTexture;
import io.github.onran0.retrogltf.loader.io.IFileProvider;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessor;
import io.github.onran0.retrogltf.loader.structure.access.GLTFBufferView;
import io.github.onran0.retrogltf.loader.structure.mesh.GLTFMesh;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
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

        GLTFBufferView[] views = this.parser.getViews();
        GLTFAccessor[] accessors = this.parser.getAccessors();
        GLTFMesh[] meshes = this.parser.getMeshes();

        BufferViewsReader viewsReader = new BufferViewsReader(
                this.parser.getBuffers(),
                views
        );

        AccessorsReader accessorsReader = new AccessorsReader(
                accessors,
                views,
                viewsReader
        );

        MeshLoader meshLoader = new MeshLoader(
                viewsReader,
                accessorsReader,
                views,
                accessors,
                meshes
        );

        IntermediateMesh[] loadedMeshes = new IntermediateMesh[meshes.length];

        for(int i = 0;i < meshes.length;i++) {
            if(meshes[i] != null) {
                loadedMeshes[i] = meshLoader.loadMesh(i);
            }
        }

        ImagesLoader imagesLoader = new ImagesLoader(
                this.parser,
                viewsReader,
                this.parser.getImages()
        );

        BufferedImage[] images = imagesLoader.loadImages();

        TexturesLoader texturesLoader = new TexturesLoader(
                this.parser.getTextures(),
                this.parser.getTextureSamplers(),
                images
        );

        GLTexture[] textures = texturesLoader.loadTextures();
    }
}