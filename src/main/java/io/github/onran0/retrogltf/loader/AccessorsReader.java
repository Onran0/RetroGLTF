package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.enums.ComponentType;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessor;
import io.github.onran0.retrogltf.loader.structure.access.GLTFAccessorType;
import io.github.onran0.retrogltf.loader.structure.access.GLTFBufferView;
import org.joml.*;

import java.nio.ByteBuffer;

class AccessorsReader {
    private final GLTFAccessor[] accessors;
    private final GLTFBufferView[] views;
    private final BufferViewsReader viewsReader;
    private final ByteBuffer tmpBuf = ByteBuffer.allocate(64);

    public AccessorsReader(
            GLTFAccessor[] accessors,
            GLTFBufferView[] views,
            BufferViewsReader viewsReader
    ) {
        this.accessors = accessors;
        this.views = views;
        this.viewsReader = viewsReader;
    }

    public int getLengthInBytes(int id) {
        GLTFAccessor accessor = accessors[id];

        return getEffectiveByteStride(accessor) * (accessor.getCount() - 1) + accessor.getElementSize();
    }

    public int getCount(int id) {
        return accessors[id].getCount();
    }

    public float getScalar(int id, int index) {
        GLTFAccessor accessor = accessors[id];

        return getComponentAsFloat(accessor, getElementPositionInView(accessor, index), 0);
    }

    public Vector2f getVec2(int id, int index) {
        GLTFAccessor accessor = accessors[id];

        int elemPos = getElementPositionInView(accessor, index);

        return new Vector2f(
                getComponentAsFloat(accessor, elemPos, 0),
                getComponentAsFloat(accessor, elemPos, 1)
        );
    }

    public Vector3f getVec3(int id, int index) {
        GLTFAccessor accessor = accessors[id];

        int elemPos = getElementPositionInView(accessor, index);

        return new Vector3f(
                getComponentAsFloat(accessor, elemPos, 0),
                getComponentAsFloat(accessor, elemPos, 1),
                getComponentAsFloat(accessor, elemPos, 2)
        );
    }

    public Vector4f getVec4(int id, int index) {
        GLTFAccessor accessor = accessors[id];

        int elemPos = getElementPositionInView(accessor, index);

        return new Vector4f(
                getComponentAsFloat(accessor, elemPos, 0),
                getComponentAsFloat(accessor, elemPos, 1),
                getComponentAsFloat(accessor, elemPos, 2),
                getComponentAsFloat(accessor, elemPos, 3)
        );
    }

    public Matrix2f getMat2(int id, int index) {
        GLTFAccessor accessor = accessors[id];

        int elemPos = getElementPositionInView(accessor, index);

        float[] m = getFloats(accessor, elemPos, 4);

        return new Matrix2f(
                m[0], m[1],
                m[2], m[3]
        );
    }

    public Matrix3f getMat3(int id, int index) {
        GLTFAccessor accessor = accessors[id];

        int elemPos = getElementPositionInView(accessor, index);

        float[] m = getFloats(accessor, elemPos, 9);

        return new Matrix3f(
                m[0], m[1], m[2],
                m[3], m[4], m[5],
                m[6], m[7], m[8]
        );
    }

    public Matrix4f getMat4(int id, int index) {
        GLTFAccessor accessor = accessors[id];

        int elemPos = getElementPositionInView(accessor, index);

        float[] m = getFloats(accessor, elemPos, 16);

        return new Matrix4f(
                m[0],  m[1],  m[2],  m[3],
                m[4],  m[5],  m[6],  m[7],
                m[8],  m[9],  m[10], m[11],
                m[12], m[13], m[14], m[15]
        );
    }

    public void getBytes(int id, ByteBuffer buf) {
        GLTFAccessor accessor = accessors[id];

        viewsReader.get(
                buf,
                accessor.getBufferView().get(),
                accessor.getByteOffset(),
                getLengthInBytes(id))
        ;
    }

    private float[] getFloats(GLTFAccessor accessor, int elemPos, int count) {
        float[] res = new float[count];

        for(int i = 0; i < count; i++) {
            res[i] = getComponentAsFloat(accessor, elemPos, i);
        }

        return res;
    }

    private float getComponentAsFloat(GLTFAccessor accessor, int elemPos, int compIndex) {
        GLTFAccessorType accessorType = accessor.getType();
        ComponentType compType = accessor.getComponentType();

        int componentSize = compType.getLength();

        // TODO: support of sparse accessors

        int viewId = accessor.getBufferView().get();

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

        viewsReader.get(tmpBuf, viewId, offset, componentSize);

        tmpBuf.position(0);

        try {
            long res;

            switch(compType) {
                case FLOAT:
                    return tmpBuf.getFloat();

                case SIGNED_BYTE:
                    res = tmpBuf.get();
                    break;

                case UNSIGNED_BYTE:
                    res = tmpBuf.get() - (long) Byte.MIN_VALUE;
                    break;

                case SIGNED_SHORT:
                    res = tmpBuf.getShort();
                    break;

                case UNSIGNED_SHORT:
                    res = tmpBuf.getShort() - (long) Short.MIN_VALUE;
                    break;

                case UNSIGNED_INT:
                    return (float) (tmpBuf.getInt() - (long) Integer.MIN_VALUE);

                default: throw new IllegalArgumentException(compType.name());
            }

            if(accessor.isNormalized() && compType.getMaxValue().isPresent()) {
                return res / (float) compType.getMaxValue().get();
            } else {
                return (float) res;
            }
        } finally {
            tmpBuf.position(0);
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
}