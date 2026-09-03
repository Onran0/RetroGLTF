package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.loader.structure.scene.GLTFNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class NodesLoader {
    private final GLTFParser parser;
    private final GLTFNode[] nodes;

    private final IntermediateMesh[] meshes;
    private final Material[] materials;

    private final Node[] outputIndexableNodes;
    private final List<Integer> skinnedNodes = new ArrayList<>();

    public NodesLoader(
            GLTFParser parser, GLTFNode[] nodes,
            IntermediateMesh[] meshes, Material[] materials
    ) {
        this.parser = parser;
        this.nodes = nodes;

        this.outputIndexableNodes = new Node[nodes.length];

        this.meshes = meshes;
        this.materials = materials;
    }

    public Node[] getOutputIndexableNodes() {
        return outputIndexableNodes;
    }

    public List<Integer> getSkinnedNodes() {
        return skinnedNodes;
    }

    private Node loadNode(int index, Node parent) {
        GLTFNode node = this.nodes[index];

        if(node.getSkin().isPresent()) {
            this.skinnedNodes.add(index);
        }

        IntermediateMesh mesh;
        Material[] materials;

        if(node.getMesh().isPresent()) {
            mesh = this.meshes[node.getMesh().get()];

            Map<Integer, Integer> globalMaterialIndexToLocalMap = mesh.getGlobalMaterialIndexToLocalMap();

            materials = new Material[globalMaterialIndexToLocalMap.size()];

            for(int globalMaterialIndex : globalMaterialIndexToLocalMap.keySet()) {
                int localMaterialIndex = globalMaterialIndexToLocalMap.get(globalMaterialIndex);

                materials[localMaterialIndex] = this.materials[globalMaterialIndex];
            }
        } else {
            mesh = null;
            materials = new Material[0];
        }

        Node outputNode = new Node(
                node.getName().get(),
                mesh != null ? mesh.getGLMesh() : null, materials,
                node.getLocalMatrix(),
                parent
        );

        this.outputIndexableNodes[index] = outputNode;

        if(node.getChildren().isPresent()) {
            for (int child : node.getChildren().get()) {
                loadNode(child, outputNode);
            }
        }

        return outputNode;
    }

    public List<Node> loadNodes() {
        Profiler.startTaskTrack(LoaderTaskType.NODES_LOADING);

        List<Node> outputNodes = new ArrayList<>();

        for(int i = 0;i < this.nodes.length;i++) {
            if(!this.parser.isNodeChildTruthTable()[i] && this.nodes[i] != null) {
                Node node = loadNode(i, null);

                node.updateGlobalMatrix();

                outputNodes.add(node);
            }
        }

        Profiler.endTaskTrack();

        return outputNodes;
    }
}