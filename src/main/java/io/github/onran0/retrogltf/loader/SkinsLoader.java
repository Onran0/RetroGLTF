package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.Skin;
import io.github.onran0.retrogltf.enums.ComponentType;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessor;
import io.github.onran0.retrogltf.loader.structure.scene.GLTFNode;
import io.github.onran0.retrogltf.loader.structure.skin.GLTFSkin;
import io.github.onran0.retrogltf.loader.util.IOUtil;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

class SkinsLoader {

    private int ubo;
    private FloatBuffer uboData;

    public int getUBO() {
        return ubo;
    }

    public FloatBuffer getUBOData() {
        return uboData;
    }

    public void loadSkins(LoadContext context, NodesLoader nodesLoader) {
        Profiler.startTaskTrack(LoaderTaskType.SKIN_LOADING);

        List<Integer> skinnedNodes = nodesLoader.getSkinnedNodes();

        Node[] indexableNodes = nodesLoader.getOutputIndexableNodes();

        GLTFParser parser = context.getParser();
        GLTFNode[] nodes = parser.getNodes();
        GLTFAccessor[] accessors = parser.getAccessors();
        GLTFSkin[] skins = parser.getSkins();

        AccessorsReader accessorsReader = context.getAccessorsReader();

        Skin[] loadedSkins = new Skin[skins.length];

        ByteBuffer fastBuf = context.popFastBuffer();

        ByteBuffer specFastBuf = fastBuf;

        for(int i = 0; i < skins.length; i++) {
            GLTFSkin skin = skins[i];

            if(skin != null) {
                int[] jointIndices = skin.getJoints();

                Node[] joints = new Node[jointIndices.length];

                for(int j = 0; j < joints.length; j++) {
                    joints[j] = indexableNodes[jointIndices[j]];
                }

                Matrix4f[] inverseBindMatrices;

                if(skin.getInverseBindMatrices().isPresent()) {
                    inverseBindMatrices = new Matrix4f[jointIndices.length];

                    int accessorId = skin.getInverseBindMatrices().get();

                    GLTFAccessor accessor = accessors[accessorId];

                    if(accessor.getComponentType() == ComponentType.FLOAT) {
                        ByteBuffer buf = IOUtil.getFastOrAlloc(
                                specFastBuf,
                                inverseBindMatrices.length * 64
                        );

                        if(buf != specFastBuf) {
                            specFastBuf = buf;
                        }

                        accessorsReader.getBytes(accessorId, buf);

                        buf.flip();

                        FloatBuffer floats = buf.asFloatBuffer();

                        for(int j = 0;j < inverseBindMatrices.length;j++) {
                            floats.position(j * 16);
                            inverseBindMatrices[j] = new Matrix4f(floats);
                        }
                    } else {
                        // TODO: faster load for non-float matrices
                        for(int j = 0;j < inverseBindMatrices.length;j++) {
                            inverseBindMatrices[j] = accessorsReader.getMat4(accessorId, j);
                        }
                    }
                } else {
                    inverseBindMatrices = null;
                }

                loadedSkins[i] = new Skin(joints, inverseBindMatrices);
            }
        }

        for(int skinnedNodeIndex : skinnedNodes) {
            Node node = indexableNodes[skinnedNodeIndex];

            node.setSkin(loadedSkins[nodes[skinnedNodeIndex].getSkin().get()]);
        }

        this.ubo = GL15.glGenBuffers();
        this.uboData = ByteBuffer
                .allocateDirect(256 * 64) // 256 mat4
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, this.ubo);

        GL15.glBufferData(
                GL31.GL_UNIFORM_BUFFER,
                this.uboData.capacity() * 4L,
                GL15.GL_DYNAMIC_DRAW
        );

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);

        context.pushFastBuffer(fastBuf);

        Profiler.endTaskTrack();
    }
}