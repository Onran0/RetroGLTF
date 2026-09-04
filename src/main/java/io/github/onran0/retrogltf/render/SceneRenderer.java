package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.Scene;
import org.joml.Matrix4f;

public class SceneRenderer {

    private final Matrix4f projMatrix = new Matrix4f();
    private final Matrix4f modelViewMatrix = new Matrix4f();
    private final Matrix4f mvpMatrix = new Matrix4f();

    private final NodeRenderer nodeRenderer;
    private final RenderSettings renderSettings;

    public SceneRenderer() {
        this(RenderSettings.DEFAULT);
    }

    public SceneRenderer(RenderSettings renderSettings) {
        if(renderSettings == null) {
            throw new IllegalArgumentException("renderSettings == null");
        }

        this.renderSettings = renderSettings;
        this.nodeRenderer = new NodeRenderer(this);
    }

    public void setRenderSettings(RenderSettings renderSettings) {
        this.renderSettings.set(renderSettings);
    }

    public RenderSettings getRenderSettings() {
        return renderSettings;
    }

    private void renderNode(Scene scene, Node node, Matrix4f mvpMatrix) {
        if(!node.isVisible())
            return;

        nodeRenderer.render(scene, node, mvpMatrix);

        for(Node child : node.getChildren()) {
            renderNode(scene, child, mvpMatrix);
        }
    }

    public void render(Scene scene, Matrix4f mvpMatrix) {
        for(Node node : scene.getNodes()) {
            renderNode(scene, node, mvpMatrix);
        }
    }
}