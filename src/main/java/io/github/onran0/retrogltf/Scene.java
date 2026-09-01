package io.github.onran0.retrogltf;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Scene {
    private final String name;
    private final List<Node> nodes;

    public Scene(final String name, final List<Node> nodes) {
        this.name = name;
        this.nodes = nodes;
    }

    public String getName() {
        return name;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
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