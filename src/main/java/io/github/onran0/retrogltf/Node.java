package io.github.onran0.retrogltf;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Node {
    private final String name;
    private final GLMesh mesh;
    private final int meshFrontFaceMode;

    private final Material[] materials;

    private final Matrix4f localMatrix;
    private final Matrix4f matrix;

    private final Vector3f position = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();
    private final Vector3f scale = new Vector3f(1, 1, 1);

    private Node parent;
    private final List<Node> children = new ArrayList<>();

    private boolean visible = true;

    public Node(String name, GLMesh mesh, Material[] materials, Matrix4f localMatrix, Node parent) {
        this.name = name;
        this.mesh = mesh;
        this.materials = materials;
        this.localMatrix = localMatrix;
        this.matrix = new Matrix4f();

        this.decomposeLocalMatrix();

        this.setParent(parent);

        this.meshFrontFaceMode = this.matrix.determinant() >= 0 ? GL11.GL_CCW : GL11.GL_CW;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
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

    public Optional<GLMesh> getMesh() {
        return Optional.ofNullable(this.mesh);
    }

    public int getFrontFaceMode() {
        return this.meshFrontFaceMode;
    }

    public void getPosition(Vector3f dst) {
        dst.set(this.position);
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);

        this.composeLocalMatrix();
        this.updateGlobalMatrix();
    }

    public void getRotation(Quaternionf dst) {
        dst.set(this.rotation);
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation.set(rotation);

        this.composeLocalMatrix();
        this.updateGlobalMatrix();
    }

    public void getEulerAngles(Vector3f dst) {
        this.rotation.getEulerAnglesXYZ(dst);
    }

    public void setEulerAngles(Vector3f eulerAngles) {
        this.rotation.rotationXYZ(eulerAngles.x(), eulerAngles.y(), eulerAngles.z());

        this.composeLocalMatrix();
        this.updateGlobalMatrix();
    }

    public void getScale(Vector3f dst) {
        dst.set(this.scale);
    }

    public void setScale(Vector3f scale) {
        this.scale.set(scale);

        this.composeLocalMatrix();
        this.updateGlobalMatrix();
    }

    public void setTRS(Vector3f position, Quaternionf rotation, Vector3f scale) {
        this.position.set(position);
        this.rotation.set(rotation);
        this.scale.set(scale);

        this.composeLocalMatrix();
        this.updateGlobalMatrix();
    }

    public void setTRS(Vector3f position, Vector3f eulerAngles, Vector3f scale) {
        this.position.set(position);
        this.scale.set(scale);
        this.rotation.rotationXYZ(eulerAngles.x(), eulerAngles.y(), eulerAngles.z());

        this.composeLocalMatrix();
        this.updateGlobalMatrix();
    }

    public void getLocalMatrix(Matrix4f dst) {
        dst.set(this.localMatrix);
    }

    public void setLocalMatrix(Matrix4f localMatrix) {
        this.localMatrix.set(localMatrix);
        this.decomposeLocalMatrix();
        this.updateGlobalMatrix();
    }

    public void getMatrix(Matrix4f dst) {
        dst.set(this.matrix);
    }

    public void updateGlobalMatrix() {
        if(parent == null) {
            matrix.set(this.localMatrix);
        } else {
            matrix.set(this.parent.matrix);
            matrix.mul(this.localMatrix);
        }

        for(Node child : children) {
            child.updateGlobalMatrix();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Node findInDirectChildren(Predicate<Node> filter) {
        for(Node child : children) {
            if(filter.test(child)) {
                return child;
            }
        }

        return null;
    }

    public Node findInChildren(Predicate<Node> filter) {
        Node res = findInDirectChildren(filter);

        if(res == null) {
            for(Node child : children) {
                res = child.findInChildren(filter);

                if(res != null) {
                    return res;
                }
            }

            return null;
        } else return res;
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

    private void composeLocalMatrix() {
        this.localMatrix.identity();
        this.localMatrix.translate(this.position);
        this.localMatrix.rotate(this.rotation);
        this.localMatrix.scale(this.scale);
    }

    private void decomposeLocalMatrix() {
        this.localMatrix.getTranslation(this.position);
        this.localMatrix.getNormalizedRotation(this.rotation);
        this.localMatrix.getScale(this.scale);
    }
}