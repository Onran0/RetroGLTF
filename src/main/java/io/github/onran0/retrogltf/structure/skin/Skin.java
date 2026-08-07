package io.github.onran0.retrogltf.structure.skin;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

public class Skin {
    private final int[] joints;
    private final int[] inverseBindMatrices;
    private final Integer skeleton;

    public Skin(JSONObject json) {
        this.joints = JSONUtil.toIntArray(json.optJSONArray("joints"));
        this.inverseBindMatrices = JSONUtil.toIntArray(json.optJSONArray("inverseBindMatrices"));
        this.skeleton = JSONUtil.getNullableInt(json, "skeleton");
    }

    public int[] getJoints() {
        return joints;
    }

    public int[] getInverseBindMatrices() {
        return inverseBindMatrices;
    }

    public boolean hasSkeleton() {
        return skeleton != null;
    }

    public int getSkeleton() {
        return skeleton;
    }
}