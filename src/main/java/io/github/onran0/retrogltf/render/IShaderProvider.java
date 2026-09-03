package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.Node;

public interface IShaderProvider {

    int getProgram(Node node);
}