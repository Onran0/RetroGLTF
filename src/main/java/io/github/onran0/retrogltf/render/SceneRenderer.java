package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.Scene;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SceneRenderer {

    private final FloatBuffer tmpBuf = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private final Matrix4f projMatrix = new Matrix4f();
    private final Matrix4f modelViewMatrix = new Matrix4f();
    private final Matrix4f mvpMatrix = new Matrix4f();

    private final NodeRenderer nodeRenderer;

    public SceneRenderer() {
        this.nodeRenderer = new NodeRenderer();
    }

    public NodeRenderer getNodeRenderer() {
        return nodeRenderer;
    }

    private void renderNode(Node node, Matrix4f mvpMatrix) {
        nodeRenderer.render(node, mvpMatrix);

        for(Node child : node.getChildren()) {
            renderNode(child, mvpMatrix);
        }
    }

    public void render(Scene scene, Matrix4f mvpMatrix) {
        for(Node node : scene.getNodes()) {
            renderNode(node, mvpMatrix);
        }
    }

    public void render(Scene scene) {
        this.tmpBuf.clear();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, this.tmpBuf);
        this.tmpBuf.rewind();

        this.projMatrix.set(this.tmpBuf);

        this.tmpBuf.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, this.tmpBuf);
        this.tmpBuf.rewind();

        this.modelViewMatrix.set(tmpBuf);

        this.projMatrix.mul(this.modelViewMatrix, this.mvpMatrix);

        render(scene, this.mvpMatrix);
    }
}