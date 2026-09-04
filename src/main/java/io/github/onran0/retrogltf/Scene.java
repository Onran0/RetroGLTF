package io.github.onran0.retrogltf;

import io.github.onran0.retrogltf.render.IShaderProvider;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Scene {
    private final String name;
    private final List<Node> nodes;

    private final int skinsUbo;
    private final FloatBuffer skinsUboData;

    public Scene(final String name, final List<Node> nodes, final int skinsUbo, final FloatBuffer skinsUboData) {
        this.name = name;
        this.nodes = nodes;

        this.skinsUbo = skinsUbo;
        this.skinsUboData = skinsUboData;
    }

    public String getName() {
        return this.name;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    public boolean hasSkinsUBO() {
        return this.skinsUbo != 0;
    }

    public int getSkinsUBO() {
        return this.skinsUbo;
    }

    public FloatBuffer getSkinsUBOData() {
        return this.skinsUboData;
    }

    public void updateShaders(IShaderProvider shaderProvider) {
        forEachEveryNode(node -> node.updateShaders(shaderProvider));
    }

    public Node findNodeByName(String name) {
        return findNode(node -> node.getName().isPresent() && node.getName().get().equals(name));
    }

    public Node findNode(Predicate<Node> filter) {
        for(Node node : this.nodes) {
            if(filter.test(node)) {
                return node;
            } else {
                Node res = node.findInChildren(filter);

                if(res != null) {
                    return res;
                }
            }
        }

        return null;
    }

    public void forEachTree(Node node, Consumer<Node> consumer) {
        consumer.accept(node);

        if(!node.getChildren().isEmpty()) {
            forEachNodesTree(node.getChildren(), consumer);
        }
    }

    public void forEachNodesTree(List<Node> nodes, Consumer<Node> consumer) {
        for(Node node : nodes) {
            consumer.accept(node);

            if(!node.getChildren().isEmpty()) {
                forEachNodesTree(node.getChildren(), consumer);
            }
        }
    }

    public void forEachEveryNode(Consumer<Node> consumer) {
        forEachNodesTree(this.nodes, consumer);
    }

    public void free() {
        forEachEveryNode(Node::free);

        if(this.skinsUbo != 0) {
            GL15.glDeleteBuffers(this.skinsUbo);
        }
    }
}