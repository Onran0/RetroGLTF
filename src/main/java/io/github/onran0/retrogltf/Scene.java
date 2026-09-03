package io.github.onran0.retrogltf;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.List;
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
}