package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.*;

import org.joml.Matrix4f;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class NodeRenderer {

    private final Matrix4f tmpNodeMatrix = new Matrix4f();
    private final Matrix4f tmpMvpMatrix = new Matrix4f();
    private final FloatBuffer matrixBuffer = ByteBuffer.allocateDirect(16 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();

    private final SceneRenderer sceneRenderer;

    public NodeRenderer(SceneRenderer sceneRenderer) {
        this.sceneRenderer = sceneRenderer;
    }

    public void render(Scene scene, Node node, Matrix4f mvpMatrix) {
        if(!node.getMesh().isPresent())
            return;

        RenderSettings renderSettings = this.sceneRenderer.getRenderSettings();

        node.getMatrix(tmpNodeMatrix);

        tmpMvpMatrix.set(mvpMatrix);

        tmpMvpMatrix.mul(tmpNodeMatrix);

        matrixBuffer.clear();

        tmpMvpMatrix.get(matrixBuffer);

        boolean hasSkin = node.getSkin().isPresent();

        int skinsUbo = 0;

        if(hasSkin) {
            skinsUbo = scene.getSkinsUBO();

            FloatBuffer uboData = scene.getSkinsUBOData();

            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, skinsUbo);

            node.getSkin().get().writeToUBO(uboData);

            GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, uboData);
        }

        GL11.glFrontFace(node.getFrontFaceMode());

        GLMeshPrimitive[] primitives = node.getMesh().get().getPrimitives();

        for(int i = 0; i < primitives.length; i++) {
            GLMeshPrimitive primitive = primitives[i];

            int program = node.getShader(i);

            int uMVPMatrixLoc = GL20.glGetUniformLocation(program, "uMVPMatrix");

            GL20.glUseProgram(program);

            GL20.glUniformMatrix4(uMVPMatrixLoc, false, matrixBuffer);

            if(hasSkin) {
                GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, skinsUbo);
            }

            boolean useCulling = false;

            if(primitive.getMaterialIndex() != -1) {
                Material material = node.getMaterial(primitive.getMaterialIndex());

                TextureInfo baseColor = material.getBaseColor();

                useCulling = material.isShouldUseCulling() || renderSettings.isForcedCulling();

                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, baseColor.getTexture().getTextureID());
            } else {
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }

            if(useCulling) {
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glCullFace(GL11.GL_BACK);
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

            if(useCulling) {
                GL11.glDisable(GL11.GL_CULL_FACE);
            }

            GL30.glBindVertexArray(0);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            GL20.glUseProgram(0);
        }

        if(hasSkin) {
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        }
    }
}