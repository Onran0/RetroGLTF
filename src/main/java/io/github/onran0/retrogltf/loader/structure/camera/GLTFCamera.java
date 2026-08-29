package io.github.onran0.retrogltf.loader.structure.camera;

import io.github.onran0.retrogltf.enums.CameraType;
import org.json.JSONObject;

import java.util.Optional;

public class GLTFCamera {
    private final String name;
    private final GLTFCameraOrthographic orthographic;
    private final GLTFCameraPerspective perspective;
    private final CameraType type;

    public GLTFCamera(JSONObject json) {
        this.name = json.optString("name");
        this.type = CameraType.getById(json.getString("type"));

        if(this.type == CameraType.ORTHOGRAPHIC) {
            this.perspective = null;
            this.orthographic = new GLTFCameraOrthographic(json.getJSONObject("orthographic"));
        } else {
            this.perspective = new GLTFCameraPerspective(json.getJSONObject("perspective"));
            this.orthographic = null;
        }
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public GLTFCameraOrthographic getOrthographic() {
        return orthographic;
    }

    public GLTFCameraPerspective getPerspective() {
        return perspective;
    }

    public CameraType getType() {
        return type;
    }
}