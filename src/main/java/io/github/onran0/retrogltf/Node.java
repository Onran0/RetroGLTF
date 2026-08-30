package io.github.onran0.retrogltf;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    private final String name;
    private final GLMesh mesh;
    private final int meshFrontFaceMode;

    private final Material[] materials;

    private final Matrix4f localMatrix;
    private final Matrix4f matrix;

    private Node parent;
    private final List<Node> children = new ArrayList<>();

    public Node(String name, GLMesh mesh, int meshFrontFaceMode, Material[] materials, Matrix4f localMatrix, Node parent) {
        this.name = name;
        this.mesh = mesh;
        this.meshFrontFaceMode = meshFrontFaceMode;
        this.materials = materials;
        this.localMatrix = localMatrix;
        this.matrix = new Matrix4f();
        this.parent = parent;
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial(int index) {
        return this.materials[index];
    }

    public void setMaterial(int index, Material material) {
        this.materials[index].set(material);
    }

    public int getMaterialsCount() {
        return this.materials.length;
    }

    public GLMesh getMesh() {
        return this.mesh;
    }

    public int getFrontFaceMode() {
        return this.meshFrontFaceMode;
    }

    public void getLocalMatrix(Matrix4f dst) {
        dst.set(this.localMatrix);
    }

    public void setLocalMatrix(Matrix4f localMatrix) {
        this.localMatrix.set(localMatrix);
        this.updateGlobalMatrix();
    }

    public void getMatrix(Matrix4f dst) {
        dst.set(this.matrix);
    }

    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public boolean isDirectChild(Node node) {
        return this.children.contains(node);
    }

    public boolean isHierarchicallyChild(Node node) {
        if (isDirectChild(node)) {
            return true;
        } else {
            for(Node child : this.children) {
                if(child.isHierarchicallyChild(node)) {
                    return true;
                }
            }

            return false;
        }
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        if(parent == this || isHierarchicallyChild(parent))
            throw new IllegalArgumentException("invalid parent");

        if(this.parent != null) {
            this.parent.children.remove(this);
        }

        this.parent = parent;

        if(parent != null) {
            parent.children.add(this);
        }

        updateGlobalMatrix();
    }

    private void updateGlobalMatrix() {
        if(parent == null) {
            matrix.set(this.localMatrix);
        } else {
            matrix.set(parent.matrix);
            matrix.mul(this.localMatrix);
        }

        for(Node child : children) {
            child.updateGlobalMatrix();
        }
    }
}