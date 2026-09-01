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

    private MeshLoader() { }

    private static void checkAttributesCompat(GLTFMeshPrimitive.Attribute[] attributes) throws GLTFLoadException {
        for(GLTFMeshPrimitive.Attribute attribute : attributes) {
            if(attribute.getRegularType() == null)
                throw new GLTFLoadException("unsupported attribute type: " + attribute.getType());
        }
    }

    private static ByteBuffer getInterleavedAttributesBuffer(
            BufferViewsReader viewsReader, AccessorsReader accessorsReader,
            GLTFBufferView[] views, GLTFAccessor[] accessors,
            ByteBuffer fastBuf,
            GLTFMeshPrimitive.Attribute[] attributes
    ) {
        int prevAttribBufferView = -1;

        int minPosInView = Integer.MAX_VALUE;
        int maxPosInView = Integer.MIN_VALUE;

        for (GLTFMeshPrimitive.Attribute attribute : attributes) {
            GLTFAccessor attribAccessor = accessors[attribute.getAccessor()];

            if (attribAccessor.getBufferView().isPresent()) {
                int bufferView = attribAccessor.getBufferView().get();

                if (
                        prevAttribBufferView != -1 && prevAttribBufferView != bufferView ||
                                !views[bufferView].getByteStride().isPresent()
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

    private static IntermediateMesh loadMesh(LoadContext context, GLTFMesh mesh) throws GLTFLoadException {
        BufferViewsReader viewsReader = context.getViewsReader();
        AccessorsReader accessorsReader = context.getAccessorsReader();

        GLTFBufferView[] views = context.getParser().getViews();
        GLTFAccessor[] accessors = context.getParser().getAccessors();

        ByteBuffer fastBuf = context.getFastBuffer();

        GLTFMeshPrimitive[] primitives = mesh.getPrimitives();

        GLMeshPrimitive[] glPrimitives = new GLMeshPrimitive[primitives.length];

        Map<Integer, Integer> globalMaterialIndexToLocalMap = new HashMap<>();

        for(int i = 0; i < primitives.length; i++) {
            GLTFMeshPrimitive primitive = primitives[i];
            GLTFMeshPrimitive.Attribute[] attributes = primitive.getAttributes();

            checkAttributesCompat(attributes);

            ByteBuffer interleavedBuf = getInterleavedAttributesBuffer(
                    viewsReader, accessorsReader,
                    views, accessors,
                    fastBuf,
                    attributes
            );

            int vao = GL30.glGenVertexArrays();
            int vbo = GL15.glGenBuffers();
            int ebo = primitive.getIndices().isPresent() ? GL15.glGenBuffers() : -1;
            int eboIndicesType = -1;
            int elementsType = primitive.getMode().getGLType();
            int verticesCount = 0;

            GL30.glBindVertexArray(vao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

            if(interleavedBuf != null) {
                GL15.glBufferData(
                        GL15.GL_ARRAY_BUFFER,
                        interleavedBuf,
                        GL15.GL_STATIC_DRAW
                );

                for (GLTFMeshPrimitive.Attribute attribute : attributes) {
                    GLTFAccessor accessor = accessors[attribute.getAccessor()];

                    int loc = attribute.getRegularType().getShaderLocation();
                    int stride = views[accessor.getBufferView().get()].getByteStride().get();

                    GL20.glVertexAttribPointer(
                            loc,
                            accessor.getType().getNumberOfComponents(),
                            accessor.getComponentType().getGLType(),
                            accessor.isNormalized(),
                            stride,
                            accessor.getByteOffset()
                    );

                    GL20.glEnableVertexAttribArray(loc);

                    verticesCount = accessor.getCount();
                }
            } else {
                int requiredBufLength = 0;

                for (GLTFMeshPrimitive.Attribute attribute : attributes) {
                    requiredBufLength += accessorsReader.getLengthInBytes(attribute.getAccessor());
                }

                ByteBuffer buf = IOUtil.getFastOrDirectAlloc(fastBuf, requiredBufLength);

                for (GLTFMeshPrimitive.Attribute attribute : attributes) {
                    accessorsReader.getBytes(attribute.getAccessor(), buf);
                }

                GL15.glBufferData(
                        GL15.GL_ARRAY_BUFFER,
                        buf,
                        GL15.GL_STATIC_DRAW
                );

                int offset = 0;

                for (GLTFMeshPrimitive.Attribute attribute : attributes) {
                    GLTFAccessor accessor = accessors[attribute.getAccessor()];

                    int loc = attribute.getRegularType().getShaderLocation();

                    GL20.glVertexAttribPointer(
                            loc,
                            accessor.getType().getNumberOfComponents(),
                            accessor.getComponentType().getGLType(),
                            accessor.isNormalized(),
                            0,
                            offset
                    );

                    GL20.glEnableVertexAttribArray(loc);

                    offset += accessorsReader.getLengthInBytes(attribute.getAccessor());

                    verticesCount = accessor.getCount();
                }
            }

            if(primitive.getIndices().isPresent()) {
                int indicesAccessorId = primitive.getIndices().get();

                GLTFAccessor indicesAccessor = accessors[indicesAccessorId];

                eboIndicesType = indicesAccessor.getComponentType().getGLType();

                int len = accessorsReader.getLengthInBytes(indicesAccessor);

                ByteBuffer buf = IOUtil.getFastOrDirectAlloc(fastBuf, len);

                buf.mark();

                accessorsReader.getBytes(indicesAccessorId, buf);

                buf.reset();

                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);

                GL15.glBufferData(
                        GL15.GL_ELEMENT_ARRAY_BUFFER,
                        buf,
                        GL15.GL_STATIC_DRAW
                );
            }

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);

            if(primitive.getIndices().isPresent()) {
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            }

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
                    elementsType, verticesCount,
                    localMaterialIndex
            );
        }

        return new IntermediateMesh(new GLMesh(glPrimitives), globalMaterialIndexToLocalMap);
    }

    public static IntermediateMesh[] loadMeshes(LoadContext context) throws GLTFLoadException {
        GLTFMesh[] meshes = context.getParser().getMeshes();

        IntermediateMesh[] loadedMeshes = new IntermediateMesh[meshes.length];

        for(int i = 0;i < meshes.length;i++) {
            GLTFMesh mesh = meshes[i];

            if(mesh != null) {
                loadedMeshes[i] = loadMesh(context, mesh);
            }
        }

        return loadedMeshes;
    }
}