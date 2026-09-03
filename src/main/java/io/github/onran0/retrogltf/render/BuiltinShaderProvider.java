package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.Node;

public class BuiltinShaderProvider implements IShaderProvider {

    private int defaultUnlit = -1;
    private int skin0256Unlit = -1;

    @Override
    public int getProgram(Node node) {
        if(node.getSkin().isPresent()) {
            if(skin0256Unlit == -1) {
                this.skin0256Unlit = BuiltinShaderLoader.getBuiltinProgram(
                        BuiltinVertexShaderType.SKIN_0_256,
                        BuiltinFragmentShaderType.UNLIT
                );
            }

            return skin0256Unlit;
        } else {
            if(defaultUnlit == -1) {
                this.defaultUnlit = BuiltinShaderLoader.getBuiltinProgram(
                        BuiltinVertexShaderType.DEFAULT,
                        BuiltinFragmentShaderType.UNLIT
                );
            }

            return defaultUnlit;
        }
    }
}