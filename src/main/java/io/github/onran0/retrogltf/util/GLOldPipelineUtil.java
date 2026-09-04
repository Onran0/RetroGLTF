package io.github.onran0.retrogltf.util;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLOldPipelineUtil {
    private static final FloatBuffer tmpBuf = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private static final Matrix4f projMatrix = new Matrix4f();
    private static final Matrix4f modelViewMatrix = new Matrix4f();

    public static void getMVPMatrix(Matrix4f mvpMatrix) {
        tmpBuf.clear();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, tmpBuf);
        tmpBuf.rewind();

        projMatrix.set(tmpBuf);

        tmpBuf.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, tmpBuf);
        tmpBuf.rewind();

        modelViewMatrix.set(tmpBuf);

        projMatrix.mul(modelViewMatrix, mvpMatrix);
    }
}