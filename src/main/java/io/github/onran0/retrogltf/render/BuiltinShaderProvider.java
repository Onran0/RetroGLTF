package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.Node;

public class BuiltinShaderProvider {

    public static int getProgram(Node node) {
        if(node.getSkin().isPresent()) {
            return BuiltinShaderLoader.getBuiltinProgram(
                    BuiltinVertexShaderType.SKIN_0_256,
                    BuiltinFragmentShaderType.UNLIT
            );
        } else {
            return BuiltinShaderLoader.getBuiltinProgram(
                    BuiltinVertexShaderType.DEFAULT,
                    BuiltinFragmentShaderType.UNLIT
            );
        }
    }
}