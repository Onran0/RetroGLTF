package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.Scene;

public class SceneRenderer {

    private final NodeRenderer nodeRenderer;

    public SceneRenderer() {
        this.nodeRenderer = new NodeRenderer();
    }

    private void renderNode(Node node) {
        nodeRenderer.render(node);

        for(Node child : node.getChildren()) {
            renderNode(child);
        }
    }

    public void render(Scene scene) {
        for(Node node : scene.getNodes()) {
            renderNode(node);
        }
    }
}