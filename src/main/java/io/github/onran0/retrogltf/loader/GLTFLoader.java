package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLTexture;
import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.Scene;
import io.github.onran0.retrogltf.loader.io.IFileProvider;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessor;
import io.github.onran0.retrogltf.loader.structure.access.GLTFBufferView;
import org.json.JSONObject;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
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

    private LoadContext getLoadContext() {
        Profiler.startTaskTrack(LoaderTaskType.LOAD_CONTEXT_INIT);

        GLTFBufferView[] views = this.parser.getViews();
        GLTFAccessor[] accessors = this.parser.getAccessors();

        BufferViewsReader viewsReader = new BufferViewsReader(
                this.parser.getBuffers(),
                views
        );

        LoadContext loadContext = new LoadContext();

        loadContext.setParser(this.parser);

        loadContext.setViewsReader(viewsReader);

        AccessorsReader accessorsReader = new AccessorsReader(
                accessors,
                views,
                viewsReader
        );

        loadContext.setAccessorsReader(accessorsReader);

        Profiler.endTaskTrack();

        return loadContext;
    }

    public Scene load() throws GLTFLoadException {
        Profiler.startTaskTrack(LoaderTaskType.SUB_LOADERS_INVOKER);

        this.parser.parse();

        LoadContext loadContext = getLoadContext();

        IntermediateMesh[] loadedMeshes = MeshLoader.loadMeshes(loadContext);

        GLTexture[] textures = TexturesLoader.loadTextures(loadContext);

        Material[] materials = MaterialsLoader.loadMaterials(loadContext, textures);

        NodesLoader nodesLoader = new NodesLoader(
                this.parser,
                this.parser.getNodes(),
                loadedMeshes, materials
        );

        List<Node> nodes = nodesLoader.loadNodes();

        int skinsUbo = 0;
        FloatBuffer skinsUboData = null;

        if(!nodesLoader.getSkinnedNodes().isEmpty()) {
            SkinsLoader skinsLoader = new SkinsLoader();

            skinsLoader.loadSkins(loadContext, nodesLoader);

            skinsUbo = skinsLoader.getUBO();
            skinsUboData = skinsLoader.getUBOData();
        }

        Profiler.endTaskTrack();

        return new Scene(
                this.parser.getScene().getName().orElse(null), nodes,
                skinsUbo, skinsUboData
        );
    }
}