package io.github.onran0.retrogltf.render;

public class RenderSettings {
    public static final RenderSettings BUILTIN = new RenderSettings(new BuiltinShaderProvider(), true);

    private IShaderProvider shaderProvider;
    private boolean forcedCulling;

    public RenderSettings(IShaderProvider shaderProvider, boolean forcedCulling) {
        this.shaderProvider = shaderProvider;
        this.forcedCulling = forcedCulling;
    }

    public IShaderProvider getShaderProvider() {
        return shaderProvider;
    }

    public boolean isForcedCulling() {
        return forcedCulling;
    }

    public void set(RenderSettings settings) {
        this.shaderProvider = settings.shaderProvider;
        this.forcedCulling = settings.forcedCulling;
    }
}