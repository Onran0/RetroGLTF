package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.GLMeshPrimitive;
import io.github.onran0.retrogltf.Node;

import io.github.onran0.retrogltf.TextureInfo;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class NodeRenderer {

    private final Matrix4f tmpNodeMatrix = new Matrix4f();
    private final Matrix4f tmpMvpMatrix = new Matrix4f();
    private final FloatBuffer matrixBuffer = ByteBuffer.allocateDirect(16 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();

    private RenderSettings renderSettings;

    private int uMatrixLoc;
    private int uBaseColorTexCoordIndexLoc;

    public NodeRenderer() {
        this(RenderSettings.BUILTIN);
    }

    public NodeRenderer(RenderSettings renderSettings) {
        this.setRenderSettings(renderSettings);
    }

    public RenderSettings getRenderSettings() {
        return renderSettings;
    }

    public void setRenderSettings(RenderSettings renderSettings) {
        this.renderSettings = renderSettings;

        int program = renderSettings.getShaderProgram();

        this.uMatrixLoc = GL20.glGetUniformLocation(program, "uMatrix");
        this.uBaseColorTexCoordIndexLoc = GL20.glGetUniformLocation(program, "uBaseColorTexCoordIndex");
    }

    public void render(Node node, Matrix4f mvpMatrix) {
        if(!node.getMesh().isPresent())
            return;

        GL20.glUseProgram(this.renderSettings.getShaderProgram());

        node.getMatrix(tmpNodeMatrix);

        tmpMvpMatrix.set(mvpMatrix);

        tmpMvpMatrix.mul(tmpNodeMatrix);

        matrixBuffer.clear();

        tmpMvpMatrix.get(matrixBuffer);

        GL20.glUniformMatrix4(this.uMatrixLoc, false, matrixBuffer);

        GL11.glFrontFace(node.getFrontFaceMode());

        for(GLMeshPrimitive primitive : node.getMesh().get().getPrimitives()) {
            if(primitive.getMaterialIndex() != -1) {
                TextureInfo baseColor = node.getMaterial(primitive.getMaterialIndex()).getBaseColor();

                GL20.glUniform1i(this.uBaseColorTexCoordIndexLoc, baseColor.getTexCoordIndex());

                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, baseColor.getTexture().getTextureID());
            } else {
                GL20.glUniform1i(this.uBaseColorTexCoordIndexLoc, 0);

                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }

            GL30.glBindVertexArray(primitive.getVAO());

            if(primitive.hasEBO()) {
                GL11.glDrawElements(
                        primitive.getElementsType(),
                        primitive.getIndicesCount(),
                        primitive.getEBOIndicesType(),
                        0
                );
            } else {
                GL11.glDrawArrays(
                        primitive.getElementsType(),
                        0, primitive.getVerticesCount()
                );
            }

            GL30.glBindVertexArray(0);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        GL20.glUseProgram(0);
    }
}