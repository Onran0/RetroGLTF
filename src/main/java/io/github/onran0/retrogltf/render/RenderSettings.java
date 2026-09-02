package io.github.onran0.retrogltf.render;

public class RenderSettings {
    public static final RenderSettings BUILTIN = new RenderSettings(BuiltinShaderLoader.getBuiltinProgram(), true);

    private int shaderProgram;
    private boolean forcedCulling;

    public RenderSettings(int shaderProgram, boolean forcedCulling) {
        this.shaderProgram = shaderProgram;
        this.forcedCulling = forcedCulling;
    }

    public int getShaderProgram() {
        return shaderProgram;
    }

    public boolean isForcedCulling() {
        return forcedCulling;
    }

    public void set(RenderSettings settings) {
        this.shaderProgram = settings.shaderProgram;
        this.forcedCulling = settings.forcedCulling;
    }
}