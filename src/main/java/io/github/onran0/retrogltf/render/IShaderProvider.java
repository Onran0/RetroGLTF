package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.GLMeshPrimitive;
import io.github.onran0.retrogltf.Node;

public interface IShaderProvider {

    int getShader(Node node, GLMeshPrimitive primitive);
}