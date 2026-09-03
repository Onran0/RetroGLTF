package io.github.onran0.retrogltf.util;

import io.github.onran0.retrogltf.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class GLCleaner {

    public static void freeMesh(GLMesh mesh) {
        for(GLMeshPrimitive prim : mesh.getPrimitives()) {
            GL15.glDeleteBuffers(prim.getVBO());

            if(prim.hasEBO())
                GL15.glDeleteBuffers(prim.getEBO());

            GL30.glDeleteVertexArrays(prim.getVAO());
        }
    }

    public static void freeMaterial(Material material) {
        freeTextureInfo(material.getBaseColor());
    }

    public static void freeTextureInfo(TextureInfo info) {
        freeTexture(info.getTexture());
    }

    public static void freeTexture(GLTexture texture) {
        GL11.glDeleteTextures(texture.getTextureID());
    }

    public static void freeNode(Node node) {
        node.getMesh().ifPresent(GLCleaner::freeMesh);

        for(int i = 0;i < node.getMaterialsCount();i++) {
            freeMaterial(node.getMaterial(i));
        }
    }

    public static void freeNodeRecursively(Node node) {
        freeNode(node);

        for(Node child : node.getChildren()) {
            freeNodeRecursively(child);
        }
    }

    public static void freeScene(Scene scene) {
        for(Node node : scene.getNodes()) {
            freeNodeRecursively(node);
        }
    }
}