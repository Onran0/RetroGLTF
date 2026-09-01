package io.github.onran0.retrogltf.render;

public class RenderSettings {
    public static final RenderSettings BUILTIN = new RenderSettings(BuiltinShaderLoader.getBuiltinProgram());

    private final int shaderProgram;

    public RenderSettings(int shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public int getShaderProgram() {
        return shaderProgram;
    }
}