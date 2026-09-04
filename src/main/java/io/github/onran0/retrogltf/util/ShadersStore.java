package io.github.onran0.retrogltf.util;

import io.github.onran0.retrogltf.GLMeshPrimitive;
import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.Node;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ShadersStore {

    private final Map<Integer, Integer> hashToGLProgram = new HashMap<>();
    private final Map<Integer, Integer> GLProgramToHash = new HashMap<>();

    private int getShaderInputHash(int shaderSpecificHash, Node node, GLMeshPrimitive primitive) {
        int attributesHash = primitive.getAttributeLocations().hashCode();

        int morphHash = primitive.hasMorphTargets()
                ? primitive.getMorphTargetsAttributeLocations().hashCode()
                : 0;

        boolean hasSkin = node.getSkin().isPresent();

        Material material = node.getMaterial(primitive.getMaterialIndex());

        int materialHash = material != null ? material.hashCode() : 0;

        return Objects.hash(shaderSpecificHash, attributesHash, morphHash, materialHash, hasSkin);
    }

    public Optional<Integer> getTargetShader(int shaderSpecificHash, Node node, GLMeshPrimitive primitive) {
        return Optional.ofNullable(this.hashToGLProgram.get(getShaderInputHash(shaderSpecificHash, node, primitive)));
    }

    public void cacheShader(int shaderSpecificHash, int program, Node node, GLMeshPrimitive primitive) {
        int hash = getShaderInputHash(shaderSpecificHash, node, primitive);

        this.hashToGLProgram.put(hash, program);
        this.GLProgramToHash.put(program, hash);
    }

    public void removeShaderFromCache(int program) {
        this.hashToGLProgram.remove(this.GLProgramToHash.get(program));
        this.GLProgramToHash.remove(program);
    }

    public void clear() {
        this.hashToGLProgram.clear();
        this.GLProgramToHash.clear();
    }

    public void free() {
        for(int shader : this.GLProgramToHash.keySet()) {
            GL20.glDeleteProgram(shader);
        }
    }
}