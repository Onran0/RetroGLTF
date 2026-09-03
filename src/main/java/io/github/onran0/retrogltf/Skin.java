package io.github.onran0.retrogltf;

import org.joml.Matrix4f;

import java.nio.FloatBuffer;

public class Skin {

    private final Node[] joints;
    private final Matrix4f[] inverseBindMatrices;

    private final Matrix4f tmpMatrix = new Matrix4f();

    public Skin(Node[] joints, Matrix4f[] inverseBindMatrices) {
        this.joints = joints;
        this.inverseBindMatrices = inverseBindMatrices;
    }

    public void writeToUBO(FloatBuffer uboDataBuffer) {
        uboDataBuffer.clear();

        for(int i = 0;i < this.joints.length;i++) {
            this.joints[i].getMatrix(this.tmpMatrix);

            if(this.inverseBindMatrices != null) {
                this.tmpMatrix.mul(this.inverseBindMatrices[i]);
            }

            this.tmpMatrix.get(i * 16, uboDataBuffer); // 16 is count of floats in mat 4x4
        }

        uboDataBuffer.rewind();
    }
}