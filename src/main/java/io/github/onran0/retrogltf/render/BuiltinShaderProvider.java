package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.GLMeshPrimitive;
import io.github.onran0.retrogltf.Node;

public class BuiltinShaderProvider {

    public static int getShader(Node node, GLMeshPrimitive primitive) {
        BuiltinVertexShaderType vertShader;
        BuiltinFragmentShaderType fragShader =  BuiltinFragmentShaderType.UNLIT;

        if(node.getSkin().isPresent()) {
            vertShader = BuiltinVertexShaderType.SKIN_0_256;
        } else {
            vertShader = BuiltinVertexShaderType.DEFAULT;
        }

        return BuiltinShaderLoader.getBuiltinProgram(
                vertShader, fragShader,
                node, primitive
        );
    }
}