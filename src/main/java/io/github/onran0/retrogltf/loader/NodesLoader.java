package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.loader.structure.scene.GLTFNode;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class NodesLoader {
    private final GLTFParser parser;
    private final GLTFNode[] nodes;

    private final IntermediateMesh[] meshes;
    private final Material[] materials;

    public NodesLoader(
            GLTFParser parser, GLTFNode[] nodes,
            IntermediateMesh[] meshes, Material[] materials
    ) {
        this.parser = parser;
        this.nodes = nodes;

        this.meshes = meshes;
        this.materials = materials;
    }

    private Matrix4f getNodeGlobalMatrix(int index) {
        Matrix4f localMatrix = nodes[index].getLocalMatrix();

        if(!parser.isNodeChildrenTruthTable()[index]) {
            return new Matrix4f(localMatrix);
        } else {
            return getNodeGlobalMatrix(parser.getNodeParentsTable()[index]).mul(localMatrix);
        }
    }

    private Node loadNode(int index, Node parent) {
        GLTFNode node = this.nodes[index];

        if(node != null) {
            int meshFrontFaceMode = getNodeGlobalMatrix(index).determinant() >= 0 ? GL11.GL_CW : GL11.GL_CCW;

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
                    node.getName().orElse(""),
                    mesh != null ? mesh.getGLMesh() : null, meshFrontFaceMode, materials,
                    node.getLocalMatrix(),
                    parent
            );

            if(node.getChildren().isPresent()) {
                for (int child : node.getChildren().get()) {
                    loadNode(child, outputNode);
                }
            }

            return outputNode;
        } else return null;
    }

    public List<Node> loadNodes() {
        List<Node> outputNodes = new ArrayList<>();

        for(int i = 0;i < this.nodes.length;i++) {
            if(!this.parser.isNodeChildrenTruthTable()[i]) {
                Node node = loadNode(i, null);

                if(node != null) {
                    node.updateGlobalMatrix();
                    outputNodes.add(node);
                }
            }
        }

        return outputNodes;
    }
}