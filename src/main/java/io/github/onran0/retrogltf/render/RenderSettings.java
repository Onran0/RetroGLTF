package io.github.onran0.retrogltf.render;

public class RenderSettings {
    public static final RenderSettings DEFAULT = new RenderSettings(true);

    private boolean forcedCulling;

    public RenderSettings(boolean forcedCulling) {
        this.forcedCulling = forcedCulling;
    }

    public boolean isForcedCulling() {
        return forcedCulling;
    }

    public void set(RenderSettings settings) {
        this.forcedCulling = settings.forcedCulling;
    }
}