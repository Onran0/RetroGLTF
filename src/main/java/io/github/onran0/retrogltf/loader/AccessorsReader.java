package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.enums.ComponentType;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessor;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessorType;
import io.github.onran0.retrogltf.loader.structure.access.GLTFBufferView;

import io.github.onran0.retrogltf.loader.structure.access.GLTFSparseAccessor;
import io.github.onran0.retrogltf.loader.util.IOUtil;
import org.joml.*;
import org.joml.Math;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

class AccessorsReader {
    private final GLTFAccessor[] accessors;
    private final GLTFBufferView[] views;
    private final BufferViewsReader viewsReader;

    private final Map<GLTFAccessor, Map<Integer, Integer>> sparseAccessorSrcElemIndexToSparseIndexMap = new HashMap<>();
    private final Map<GLTFAccessor, int[]> sparseAccessorIndicesMap = new HashMap<>();

    private final ByteBuffer fastBuf;

    public AccessorsReader(
            GLTFAccessor[] accessors,
            GLTFBufferView[] views,
            BufferViewsReader viewsReader,
            ByteBuffer fastBuf
    ) {
        this.accessors = accessors;
        this.views = views;
        this.viewsReader = viewsReader;
        this.fastBuf = fastBuf;

        for (GLTFAccessor accessor : accessors) {
            accessor.getSparse().ifPresent(sparseAccessor -> {
                Map<Integer, Integer> mappedElementsIndices = new HashMap<>();

                int sparseElemsCount = sparseAccessor.getCount();

                int[] sparseAccessorIndices = new int[sparseElemsCount];

                int indicesView = sparseAccessor.getIndicesBufferView();
                int indicesOffsetInView = sparseAccessor.getIndicesByteOffset();
                ComponentType indicesCompType = sparseAccessor.getIndicesComponentType();

                int indicesLength = indicesCompType.getLength() * sparseElemsCount;

                int requiredLength = indicesCompType.getLength() * sparseElemsCount;

                // TODO: multi batching for more fast load without heap allocations
                ByteBuffer tmpBuf = IOUtil.getFastOrAlloc(fastBuf, requiredLength);

                viewsReader.get(
                        tmpBuf, indicesView,
                        indicesOffsetInView, indicesLength
                );

                tmpBuf.flip();

                for (int j = 0; j < sparseElemsCount; j++) {
                    int elemIndex = (int) getIntegerComponent(tmpBuf, indicesCompType);

                    mappedElementsIndices.put(elemIndex, j);

                    sparseAccessorIndices[j] = elemIndex;
                }

                tmpBuf.position(0);

                sparseAccessorSrcElemIndexToSparseIndexMap.put(accessor, mappedElementsIndices);
                sparseAccessorIndicesMap.put(accessor, sparseAccessorIndices);
            });
        }
    }

    public int getLengthInBytes(GLTFAccessor accessor) {
        return getEffectiveByteStride(accessor) * (accessor.getCount() - 1) + accessor.getElementSize();
    }

    public int getLengthInBytes(int id) {
        return getLengthInBytes(accessors[id]);
    }

    public int getElementsCount(int id) {
        return accessors[id].getCount();
    }

    public float getFloatScalar(int id, int index) {
        return getComponentAsFloat(accessors[id], index, 0);
    }

    public Vector2f getVec2(int id, int index) {
        float[] v = getFloats(accessors[id], index, 2);

        return new Vector2f(v[0], v[1]);
    }

    public Vector3f getVec3(int id, int index) {
        float[] v = getFloats(accessors[id], index, 3);

        return new Vector3f(v[0], v[1], v[2]);
    }

    public Vector4f getVec4(int id, int index) {
        float[] v = getFloats(accessors[id], index, 4);

        return new Vector4f(v[0], v[1], v[2], v[3]);
    }

    public Matrix2f getMat2(int id, int index) {
        float[] m = getFloats(accessors[id], index, 4);

        return new Matrix2f(
                m[0], m[1],
                m[2], m[3]
        );
    }

    public Matrix3f getMat3(int id, int index) {
        float[] m = getFloats(accessors[id], index, 9);

        return new Matrix3f(
                m[0], m[1], m[2],
                m[3], m[4], m[5],
                m[6], m[7], m[8]
        );
    }

    public Matrix4f getMat4(int id, int index) {
        float[] m = getFloats(accessors[id], index, 16);

        return new Matrix4f(
                m[0],  m[1],  m[2],  m[3],
                m[4],  m[5],  m[6],  m[7],
                m[8],  m[9],  m[10], m[11],
                m[12], m[13], m[14], m[15]
        );
    }

    public void getBytes(int id, ByteBuffer buf) {
        GLTFAccessor accessor = accessors[id];

        int srcPos = buf.position();
        int lengthInBytes = getLengthInBytes(id);
        int endPos = srcPos + lengthInBytes;

        if(accessor.getBufferView().isPresent()) {
            viewsReader.get(
                    buf,
                    accessor.getBufferView().get(),
                    accessor.getByteOffset(),
                    lengthInBytes
            );
        } else {
            IOUtil.fillBufferWithZeros(buf, lengthInBytes);
        }

        if(accessor.getSparse().isPresent()) {
            buf.position(srcPos);

            GLTFSparseAccessor sparseAccessor = accessor.getSparse().get();

            int requiredCapacityForSparseValues = accessor.getElementSize() * sparseAccessor.getCount();

            // TODO: multi batching for more fast load without heap allocations
            ByteBuffer sparseValuesBuffer = IOUtil.getFastOrAlloc(fastBuf, requiredCapacityForSparseValues);

            viewsReader.get(
                    sparseValuesBuffer,
                    sparseAccessor.getValuesBufferView(),
                    sparseAccessor.getValuesByteOffset(),
                    requiredCapacityForSparseValues
            );

            sparseValuesBuffer.flip();

            int elementSize = accessor.getElementSize();

            int prevSparseBufLimit = sparseValuesBuffer.limit();

            int[] sparseAccessorIndices = sparseAccessorIndicesMap.get(accessor);

            for(int sparseElemIndex = 0;sparseElemIndex < sparseAccessorIndices.length; sparseElemIndex++) {
                int srcElemIndex = sparseAccessorIndices[sparseElemIndex];

                int sparseValuePos = sparseElemIndex * elementSize;

                sparseValuesBuffer.position(sparseValuePos);
                sparseValuesBuffer.limit(sparseValuePos + elementSize);

                buf.position(srcPos + getElementPositionInView(accessor, srcElemIndex));
                buf.put(sparseValuesBuffer);
            }

            sparseValuesBuffer.position(0);
            sparseValuesBuffer.limit(prevSparseBufLimit);
        }

        buf.position(endPos);
    }

    private float[] getFloats(GLTFAccessor accessor, int elemPos, int count) {
        float[] res = new float[count];

        for(int i = 0; i < count; i++) {
            res[i] = getComponentAsFloat(accessor, elemPos, i);
        }

        return res;
    }

    private float getComponentAsFloat(GLTFAccessor accessor, int elemIndex, int compIndex) {
        GLTFAccessorType accessorType = accessor.getType();
        ComponentType compType = accessor.getComponentType();

        int componentSize = compType.getLength();

        int viewId = -1;
        int elemPos = -1;

        boolean getFromDefaultView = true;

        if(accessor.getSparse().isPresent()) {
            Map<Integer, Integer> srcIndexToSparseIndex = sparseAccessorSrcElemIndexToSparseIndexMap.get(accessor);

            if(srcIndexToSparseIndex.containsKey(elemIndex)) {
                int sparseIndex = srcIndexToSparseIndex.get(elemIndex);

                viewId = accessor.getSparse().get().getValuesBufferView();
                elemPos = getSparseElementPositionInView(accessor, sparseIndex);

                getFromDefaultView = false;
            } else if(!accessor.getBufferView().isPresent()) {
                return 0.0f;
            }
        }

        if(getFromDefaultView && accessor.getBufferView().isPresent()) {
            viewId = accessor.getBufferView().get();
            elemPos = getElementPositionInView(accessor, elemIndex);
        }

        int offset = elemPos;

        if(!accessorType.isMatrixType()) {
            offset += compIndex * componentSize;
        } else {
            int dim = accessorType.getMatrixDimension();

            int columnPaddingBytes = (componentSize * dim) % 4;
            int columnLengthInBytes = componentSize * dim + columnPaddingBytes;

            int column = compIndex / dim;
            int row = compIndex % dim;

            offset += columnLengthInBytes * column + row * componentSize;
        }

        fastBuf.rewind();

        viewsReader.get(fastBuf, viewId, offset, componentSize);

        fastBuf.rewind();

        float res = getComponentAsFloat(fastBuf, compType, accessor.isNormalized());

        fastBuf.rewind();

        return res;
    }

    private float getComponentAsFloat(ByteBuffer buf, ComponentType compType, boolean normalized) {
        if(compType == ComponentType.FLOAT) {
            return buf.getFloat();
        } else {
            long intComp = getIntegerComponent(buf, compType);

            if(compType == ComponentType.UNSIGNED_INT || !normalized) {
                return intComp;
            } else {
                return Math.clamp(-1.0F, 1.0F, intComp / (float) compType.getMaxValue());
            }
        }
    }

    private long getIntegerComponent(ByteBuffer buf, ComponentType compType) {
        switch(compType) {
            case SIGNED_BYTE:
                return buf.get();

            case UNSIGNED_BYTE:
                return buf.get() & 0xFF;

            case SIGNED_SHORT:
                return buf.getShort();

            case UNSIGNED_SHORT:
                return buf.getShort() & 0xFFFF;

            case UNSIGNED_INT:
                return buf.getInt() & 0xFFFFFFFFL;

            default: throw new IllegalArgumentException(compType.name());
        }
    }

    private int getEffectiveByteStride(GLTFAccessor accessor) {
        if(accessor.getBufferView().isPresent()) {
            return views[accessor.getBufferView().get()].getByteStride().orElse(accessor.getElementSize());
        } else {
            return accessor.getElementSize();
        }
    }

    private int getElementPositionInView(GLTFAccessor accessor, int index) {
        return index * getEffectiveByteStride(accessor) + accessor.getByteOffset();
    }

    private int getSparseElementPositionInView(GLTFAccessor accessor, int index) {
        return index * accessor.getElementSize() + accessor.getSparse().get().getValuesByteOffset();
    }
}