package io.github.onran0.retrogltf.loader.structure.skin;

import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFSkin {
    private final int[] joints;
    private final Integer inverseBindMatrices;
    private final Integer skeleton;

    public GLTFSkin(JSONObject json) {
        this.joints = JSONUtil.toIntArray(json.optJSONArray("joints"));
        this.inverseBindMatrices = JSONUtil.getNullableInt(json, "inverseBindMatrices");
        this.skeleton = JSONUtil.getNullableInt(json, "skeleton");
    }

    public int[] getJoints() {
        return this.joints;
    }

    public Optional<Integer> getInverseBindMatrices() {
        return Optional.ofNullable(this.inverseBindMatrices);
    }

    public Optional<Integer> getSkeleton() {
        return Optional.ofNullable(this.skeleton);
    }
}