package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLTexture;
import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.Scene;
import io.github.onran0.retrogltf.loader.io.IFileProvider;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessor;
import io.github.onran0.retrogltf.loader.structure.access.GLTFBufferView;
import io.github.onran0.retrogltf.loader.structure.mesh.GLTFMesh;
import org.json.JSONObject;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;

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

    public Scene load() throws GLTFLoadException {
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

        TexturesLoader texturesLoader = new TexturesLoader(
                this.parser.getTextures(),
                this.parser.getTextureSamplers(),
                imagesLoader.loadImages()
        );

        GLTexture[] textures = texturesLoader.loadTextures();

        MaterialsLoader materialsLoader = new MaterialsLoader(
                this.parser.getMaterials(),
                textures
        );

        Material[] materials = materialsLoader.loadMaterials();

        NodesLoader nodesLoader = new NodesLoader(
                this.parser,
                this.parser.getNodes(),
                loadedMeshes, materials
        );

        List<Node> nodes = nodesLoader.loadNodes();

        return new Scene(this.parser.getScene().getName().orElse(null), nodes);
    }
}