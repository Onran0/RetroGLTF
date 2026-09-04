package io.github.onran0.retrogltf;

import io.github.onran0.retrogltf.enums.PrimitiveAttributeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GLMeshPrimitive {
    private final int vao;
    private final int vbo;

    private final int ebo;
    private final int eboIndicesType;

    private final int elementsType;
    private final int verticesCount;
    private final int indicesCount;

    private final int materialIndex;

    private final Map<PrimitiveAttributeType, Integer> attributeLocations;
    private final List<Map<PrimitiveAttributeType, Integer>> morphTargetsAttributeLocations;

    public GLMeshPrimitive(
            int vao, int vbo,
            int ebo, int eboIndicesType,
            int elementsType, int verticesCount, int indicesCount,
            int materialIndex,
            Map<PrimitiveAttributeType, Integer> attributeLocations,
            List<Map<PrimitiveAttributeType, Integer>> morphTargetsAttributeLocations
    ) {
        this.vao = vao;
        this.vbo = vbo;

        this.ebo = ebo;
        this.eboIndicesType = eboIndicesType;

        this.elementsType = elementsType;
        this.verticesCount = verticesCount;
        this.indicesCount = indicesCount;

        this.materialIndex = materialIndex;

        this.attributeLocations = Collections.unmodifiableMap(attributeLocations);

        if(morphTargetsAttributeLocations != null) {
            List<Map<PrimitiveAttributeType, Integer>> immutableMorphsLocs = new ArrayList<>();

            for(Map<PrimitiveAttributeType, Integer> morphLocs : morphTargetsAttributeLocations) {
                immutableMorphsLocs.add(Collections.unmodifiableMap(morphLocs));
            }

            this.morphTargetsAttributeLocations = Collections.unmodifiableList(immutableMorphsLocs);
        } else {
            this.morphTargetsAttributeLocations = null;
        }
    }

    public boolean hasEBO() {
        return this.ebo != -1;
    }

    public int getVBO() {
        return this.vbo;
    }

    public int getVAO() {
        return this.vao;
    }

    public int getEBO() {
        return this.ebo;
    }

    public int getEBOIndicesType() {
        return this.eboIndicesType;
    }

    public int getElementsType() {
        return this.elementsType;
    }

    public int getVerticesCount() {
        return this.verticesCount;
    }

    public int getIndicesCount() {
        return this.indicesCount;
    }

    public int getMaterialIndex() {
        return this.materialIndex;
    }

    public Map<PrimitiveAttributeType, Integer> getAttributeLocations() {
        return this.attributeLocations;
    }

    public List<Map<PrimitiveAttributeType, Integer>> getMorphTargetsAttributeLocations() {
        return this.morphTargetsAttributeLocations;
    }

    public boolean hasMorphTargets() {
        return this.morphTargetsAttributeLocations != null && !morphTargetsAttributeLocations.isEmpty();
    }
}