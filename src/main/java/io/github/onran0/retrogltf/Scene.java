package io.github.onran0.retrogltf;

import java.util.Collections;
import java.util.List;

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
}