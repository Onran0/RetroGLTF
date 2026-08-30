package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLMesh;
import io.github.onran0.retrogltf.GLMeshPrimitive;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessor;
import io.github.onran0.retrogltf.loader.structure.access.GLTFBufferView;
import io.github.onran0.retrogltf.loader.structure.mesh.GLTFMesh;
import io.github.onran0.retrogltf.loader.structure.mesh.GLTFMeshPrimitive;
import io.github.onran0.retrogltf.loader.util.IOUtil;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

class MeshLoader {

    private final BufferViewsReader viewsReader;
    private final AccessorsReader accessorsReader;
    private final GLTFBufferView[] views;
    private final GLTFAccessor[] accessors;
    private final GLTFMesh[] meshes;

    private final ByteBuffer fastBuf = ByteBuffer.allocateDirect(1024 * 1024);

    public MeshLoader(
            BufferViewsReader viewsReader, AccessorsReader accessorsReader,
            GLTFBufferView[] views, GLTFAccessor[] accessors,
            GLTFMesh[] meshes
    ) {
        this.viewsReader = viewsReader;
        this.accessorsReader = accessorsReader;

        this.views = views;
        this.accessors = accessors;

        this.meshes = meshes;
    }

    private void checkAttributesCompat(GLTFMeshPrimitive.Attribute[] attributes) throws GLTFLoadException {
        for(GLTFMeshPrimitive.Attribute attribute : attributes) {
            if(attribute.getRegularType() == null)
                throw new GLTFLoadException("unsupported attribute type: " + attribute.getType());
        }
    }

    private ByteBuffer getInterleavedAttributesBuffer(GLTFMeshPrimitive.Attribute[] attributes) {
        int prevAttribBufferView = -1;

        int minPosInView = Integer.MAX_VALUE;
        int maxPosInView = Integer.MIN_VALUE;

        for (GLTFMeshPrimitive.Attribute attribute : attributes) {
            GLTFAccessor attribAccessor = this.accessors[attribute.getAccessor()];

            if (attribAccessor.getBufferView().isPresent()) {
                int bufferView = attribAccessor.getBufferView().get();

                if (
                        prevAttribBufferView != -1 && prevAttribBufferView != bufferView ||
                                !this.views[bufferView].getByteStride().isPresent()
                ) {
                    return null;
                }

                int accessorStartInView = attribAccessor.getByteOffset();
                int accessorEndInView = accessorStartInView + accessorsReader.getLengthInBytes(attribAccessor);

                if(accessorStartInView < minPosInView) {
                    minPosInView = accessorStartInView;
                }

                if(accessorEndInView > maxPosInView) {
                    maxPosInView = accessorEndInView;
                }

                prevAttribBufferView = bufferView;
            } else {
                return null;
            }
        }

        int len = maxPosInView - minPosInView;

        ByteBuffer buf = IOUtil.getFastOrDirectAlloc(fastBuf, len);

        viewsReader.get(buf, prevAttribBufferView, minPosInView, len);

        return buf;
    }

    public IntermediateMesh loadMesh(int id) throws GLTFLoadException {
        GLTFMesh mesh = this.meshes[id];

        GLTFMeshPrimitive[] primitives = mesh.getPrimitives();

        GLMeshPrimitive[] glPrimitives = new GLMeshPrimitive[primitives.length];

        Map<Integer, Integer> globalMaterialIndexToLocalMap = new HashMap<>();

        for(int i = 0; i < primitives.length; i++) {
            GLTFMeshPrimitive primitive = primitives[i];
            GLTFMeshPrimitive.Attribute[] attributes = primitive.getAttributes();

            checkAttributesCompat(attributes);

            ByteBuffer interleavedBuf = getInterleavedAttributesBuffer(attributes);

            int vao;
            int vbo;
            int ebo;
            int eboIndicesType;
            int elementsType;

            if(interleavedBuf != null) {
                elementsType = primitive.getMode().getGLType();

                vao = GL30.glGenVertexArrays();
                vbo = GL15.glGenBuffers();
                ebo = primitive.getIndices().isPresent() ? GL15.glGenBuffers() : -1;
                eboIndicesType = -1;

                GL30.glBindVertexArray(vao);

                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

                GL15.glBufferData(
                        GL15.GL_ARRAY_BUFFER,
                        interleavedBuf,
                        GL15.GL_STATIC_DRAW
                );

                for(int j = 0;j < attributes.length;j++) {
                    GLTFMeshPrimitive.Attribute attribute = attributes[j];

                    GLTFAccessor accessor = this.accessors[attribute.getAccessor()];

                    int stride = this.views[accessor.getBufferView().get()].getByteStride().get();

                    GL20.glVertexAttribPointer(
                            j, accessor.getType().getNumberOfComponents(),
                            accessor.getComponentType().getGLType(),
                            accessor.isNormalized(),
                            stride,
                            accessor.getByteOffset()
                    );

                    GL20.glEnableVertexAttribArray(j);
                }

                if(primitive.getIndices().isPresent()) {
                    int indicesAccessorId = primitive.getIndices().get();

                    GLTFAccessor indicesAccessor = this.accessors[indicesAccessorId];

                    eboIndicesType = indicesAccessor.getComponentType().getGLType();

                    int len = this.accessorsReader.getLengthInBytes(indicesAccessor);

                    ByteBuffer buf = IOUtil.getFastOrDirectAlloc(fastBuf, len);

                    this.accessorsReader.getBytes(indicesAccessorId, buf);

                    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);

                    GL15.glBufferData(
                            GL15.GL_ELEMENT_ARRAY_BUFFER,
                            buf,
                            GL15.GL_STATIC_DRAW
                    );
                }

                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
                GL30.glBindVertexArray(0);

                // TODO: support of loading any meshes
            } else throw new GLTFLoadException("not interleaved packed mesh primitive attributes is not supported");

            int localMaterialIndex = -1;

            if(primitive.getMaterial().isPresent()) {
                int globalMatIndex = primitive.getMaterial().get();

                if(!globalMaterialIndexToLocalMap.containsKey(globalMatIndex)) {
                    localMaterialIndex = globalMaterialIndexToLocalMap.size();

                    globalMaterialIndexToLocalMap.put(
                            globalMatIndex,
                            localMaterialIndex
                    );
                } else localMaterialIndex = globalMaterialIndexToLocalMap.get(globalMatIndex);
            }

            glPrimitives[i] = new GLMeshPrimitive(
                    vao, vbo,
                    ebo, eboIndicesType,
                    elementsType,
                    localMaterialIndex
            );
        }

        return new IntermediateMesh(new GLMesh(glPrimitives), globalMaterialIndexToLocalMap);
    }
}