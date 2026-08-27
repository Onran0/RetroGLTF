package io.github.onran0.retrogltf.structure.skin;

import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFSkin {
    private final int[] joints;
    private final int[] inverseBindMatrices;
    private final Integer skeleton;

    public GLTFSkin(JSONObject json) {
        this.joints = JSONUtil.toIntArray(json.optJSONArray("joints"));
        this.inverseBindMatrices = JSONUtil.toIntArray(json.optJSONArray("inverseBindMatrices"));
        this.skeleton = JSONUtil.getNullableInt(json, "skeleton");
    }

    public int[] getJoints() {
        return joints;
    }

    public Optional<int[]> getInverseBindMatrices() {
        return Optional.ofNullable(inverseBindMatrices);
    }

    public Optional<Integer> getSkeleton() {
        return Optional.ofNullable(skeleton);
    }
}