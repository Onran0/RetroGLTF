package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.loader.structure.access.*;
import io.github.onran0.retrogltf.loader.structure.animation.GLTFAnimation;
import io.github.onran0.retrogltf.loader.structure.asset.GLTFAsset;
import io.github.onran0.retrogltf.loader.structure.camera.GLTFCamera;
import io.github.onran0.retrogltf.loader.structure.material.*;
import io.github.onran0.retrogltf.loader.structure.mesh.*;
import io.github.onran0.retrogltf.loader.structure.scene.GLTFNode;
import io.github.onran0.retrogltf.loader.structure.scene.*;
import io.github.onran0.retrogltf.loader.structure.skin.GLTFSkin;
import io.github.onran0.retrogltf.loader.structure.texture.*;

import io.github.onran0.retrogltf.loader.io.*;
import io.github.onran0.retrogltf.loader.util.JSONUtil;

import org.json.*;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;

class GLTFParser {
    public static final int MIN_SUPPORTED_MAJOR_VERSION = 2;
    public static final int MIN_SUPPORTED_MINOR_VERSION = 0;

    public static final int MAX_SUPPORTED_MAJOR_VERSION = 2;
    public static final int MAX_SUPPORTED_MINOR_VERSION = 0;

    private final JSONObject root;
    private final IFileProvider fileProvider;
    private final ByteBuffer glbBuffer;

    /*       General GLTF properties      */

    private ByteBuffer[] buffers;
    private GLTFBufferView[] views;
    private GLTFAccessor[] accessors;

    private GLTFNode[] nodes;

    private GLTFCamera[] cameras;

    private GLTFMesh[] meshes;
    private GLTFSkin[] skins;

    private GLTFAnimation[] animations;

    private GLTFMaterial[] materials;
    private GLTFTexture[] textures;
    private GLTFTextureSampler[] textureSamplers;
    private GLTFImage[] images;

    /*         Raw JSON properties        */

    private JSONArray nodesJson;
    private JSONArray camerasJson;
    private JSONArray meshesJson;
    private JSONArray skinsJson;
    private JSONArray animationsJson;
    private JSONArray materialsJson;
    private JSONArray texturesJson;
    private JSONArray textureSamplersJson;
    private JSONArray imagesJson;

    /* Utility properties for performance */

    private int[] nodeParents;
    private boolean[] isNodeChildren;

    /*                                    */

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

    private ByteBuffer getBuffer(String srcUri, Integer byteLength) throws IOException {
        String uri = URLDecoder.decode(srcUri, "UTF-8");

        if(uri.startsWith("data:")) {
            return ByteBuffer.wrap(
                    Base64.getDecoder().decode(uri.substring( uri.indexOf(";base64,") + 9))
            );
        } else {
            int colon = uri.indexOf("/");

            String firstSegment = colon != -1 ? uri.substring(0, colon - 1) : uri;

            if(!firstSegment.contains(":")) {
                if(this.fileProvider == null)
                    throw new IllegalStateException("File provider is undefined");

                return this.fileProvider.getFileData(uri, byteLength);
            } else {
                throw new IllegalArgumentException("Unsupported buffer URI: " + srcUri);
            }
        }
    }

    public ByteBuffer[] getBuffers() {
        return buffers;
    }

    public GLTFBufferView[] getViews() {
        return views;
    }

    public GLTFAccessor[] getAccessors() {
        return accessors;
    }

    public GLTFNode[] getNodes() {
        return nodes;
    }

    public GLTFCamera[] getCameras() {
        return cameras;
    }

    public GLTFMesh[] getMeshes() {
        return meshes;
    }

    public GLTFSkin[] getSkins() {
        return skins;
    }

    public GLTFAnimation[] getAnimations() {
        return animations;
    }

    public GLTFMaterial[] getMaterials() {
        return materials;
    }

    public GLTFTexture[] getTextures() {
        return textures;
    }

    public GLTFTextureSampler[] getTextureSamplers() {
        return textureSamplers;
    }

    public GLTFImage[] getImages() {
        return images;
    }

    public boolean[] isNodeChildrenTruthTable() {
        return isNodeChildren;
    }

    public int[] getNodeParentsTable() {
        return nodeParents;
    }

    private void initStructure() {
        nodesJson = root.optJSONArray("nodes");
        camerasJson = root.optJSONArray("cameras");
        meshesJson = root.optJSONArray("meshes");
        skinsJson = root.optJSONArray("skins");
        animationsJson = root.optJSONArray("animations");
        materialsJson = root.optJSONArray("materials");
        texturesJson = root.optJSONArray("textures");
        textureSamplersJson = root.optJSONArray("samplers");
        imagesJson = root.optJSONArray("images");

        nodeParents = new int[nodesJson.length()];
        isNodeChildren = new boolean[nodesJson.length()];

        nodes = new GLTFNode[nodesJson == null ? 0 : nodesJson.length()];
        cameras = new GLTFCamera[camerasJson == null ? 0 : camerasJson.length()];
        meshes = new GLTFMesh[meshesJson == null ? 0 : meshesJson.length()];
        skins = new GLTFSkin[skinsJson == null ? 0 : skinsJson.length()];
        animations = new GLTFAnimation[animationsJson == null ? 0 : animationsJson.length()];
        materials = new GLTFMaterial[materialsJson == null ? 0 : materialsJson.length()];
        textures = new GLTFTexture[texturesJson == null ? 0 : texturesJson.length()];
        textureSamplers = new GLTFTextureSampler[textureSamplersJson == null ? 0 : textureSamplersJson.length()];
        images = new GLTFImage[imagesJson == null ? 0 : imagesJson.length()];
    }

    private void parseUsedGLTFProperties(int sceneIndex) {
        GLTFScene scene = new GLTFScene(
                root.getJSONArray("scenes").getJSONObject(sceneIndex)
        );

        scene.getNodes().ifPresent(nodes -> {
            for(int usedNodeIndex : nodes) {
                parseNode(usedNodeIndex);
            }
        });

        if(animationsJson != null) {
            for(int i = 0;i < animationsJson.length();i++) {
                parseAnimation(i);
            }
        }
    }

    private void parseAnimation(int animationIndex) {
        animations[animationIndex] = new GLTFAnimation(animationsJson.getJSONObject(animationIndex));
    }

    private void parseNode(int nodeIndex) {
        GLTFNode node = nodes[nodeIndex] = new GLTFNode(nodesJson.getJSONObject(nodeIndex));

        node.getChildren().ifPresent(children -> {
            for(int child : children) {
                nodeParents[child] = nodeIndex;
                isNodeChildren[child] = true;
            }
        });

        node.getMesh().ifPresent(this::parseMesh);
        node.getSkin().ifPresent(this::parseSkin);
        node.getCamera().ifPresent(this::parseCamera);
    }

    private void parseMesh(int meshIndex) {
        GLTFMesh mesh = meshes[meshIndex] = new GLTFMesh(meshesJson.getJSONObject(meshIndex));

        for(GLTFMeshPrimitive primitive : mesh.getPrimitives()) {
            primitive.getMaterial().ifPresent(this::parseMaterial);
        }
    }

    private void parseCamera(int cameraIndex) {
        cameras[cameraIndex] = new GLTFCamera(camerasJson.getJSONObject(cameraIndex));
    }

    private void parseSkin(int skinIndex) {
        skins[skinIndex] = new GLTFSkin(skinsJson.getJSONObject(skinIndex));
    }

    private void parseMaterial(int matIndex) {
        GLTFMaterial material = materials[matIndex] = new GLTFMaterial(
                materialsJson.getJSONObject(matIndex)
        );

        GLTFPBRMetallicRoughness pbr = material.getPBRMetallicRoughness();

        Stream.of(
                material.getEmissiveTexture(),
                material.getNormalTexture(),
                material.getOcclusionTexture(),
                pbr.getBaseColorTexture(),
                pbr.getMetallicRoughnessTexture()
        ).forEach(tex -> tex.ifPresent(this::parseTextureByInfo));
    }

    private void parseTextureByInfo(GLTFTextureInfo texInfo) {
        int texIndex = texInfo.getIndex();

        GLTFTexture texture = textures[texIndex] = new GLTFTexture(
                texturesJson.getJSONObject(texIndex)
        );

        texture.getSampler().ifPresent(this::parseSampler);
        texture.getSource().ifPresent(this::parseImage);
    }

    private void parseSampler(int sampIndex) {
        textureSamplers[sampIndex] = new GLTFTextureSampler(
                textureSamplersJson.getJSONObject(sampIndex)
        );
    }

    private void parseImage(int imgIndex) {
        images[imgIndex] = new GLTFImage(imagesJson.getJSONObject(imgIndex));
    }

//    private Matrix4f getNodeGlobalMatrix(int index) {
//        Matrix4f localMatrix = nodes[index].getLocalMatrix();
//
//        if(!isNodeChildren[index]) {
//            return new Matrix4f(localMatrix);
//        } else {
//            return getNodeGlobalMatrix(nodeParents[index]).mul(localMatrix);
//        }
//    }

    public void parse() throws GLTFLoadException {
        GLTFAsset asset = new GLTFAsset(root.getJSONObject("asset"));

        if(
                asset.getMajorVersion() > MAX_SUPPORTED_MAJOR_VERSION ||
                        asset.getMajorVersion() < MIN_SUPPORTED_MAJOR_VERSION)
        {
            throw new GLTFLoadException(String.format(
                    "glTF files of version %d.x is not supported", asset.getMajorVersion()
            ));
        } else if(
                asset.getMinVersion().isPresent() &&
                        (
                                asset.getMajorMinVersion() > MAX_SUPPORTED_MAJOR_VERSION ||
                                        asset.getMinorMinVersion() > MAX_SUPPORTED_MINOR_VERSION
                        )
        ) {
            throw new GLTFLoadException(String.format(
                    "file requires support of glTF %d.%d but loader supports files with version <= %d.%d",
                    asset.getMajorMinVersion(), asset.getMinorMinVersion(),
                    MAX_SUPPORTED_MAJOR_VERSION, MAX_SUPPORTED_MINOR_VERSION
            ));
        }

        JSONArray buffersJson = root.getJSONArray("buffers");

        int buffersCount = buffersJson.length();

        buffers = new ByteBuffer[buffersCount];

        for(int i = 0;i < buffersCount;i++) {
            GLTFBuffer bufferObj = new GLTFBuffer(buffersJson.getJSONObject(i));

            Optional<String> uri = bufferObj.getURI();
            int byteLength = bufferObj.getByteLength();

            if(!uri.isPresent()) {
                if(this.glbBuffer == null)
                    throw new IllegalStateException("GLB buffer is undefined");

                this.glbBuffer.limit(byteLength - 1);
                buffers[i] = glbBuffer;
            } else {
                try {
                    buffers[i] = getBuffer(uri.get(), byteLength);
                } catch(IOException e) {
                    throw new GLTFLoadException(e);
                }
            }
        }

        views = JSONUtil.toObjectArray(
                root.getJSONArray("bufferViews"),
                GLTFBufferView[]::new,
                GLTFBufferView::new
        );

        accessors = JSONUtil.toObjectArray(
                root.getJSONArray("accessors"),
                GLTFAccessor[]::new,
                GLTFAccessor::new
        );

        initStructure();
        parseUsedGLTFProperties(root.getInt("scene"));
    }
}