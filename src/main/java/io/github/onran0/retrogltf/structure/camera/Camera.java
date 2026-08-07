package io.github.onran0.retrogltf.structure.camera;

import io.github.onran0.retrogltf.constants.CameraType;
import org.json.JSONObject;

public class Camera {
    private final String name;
    private final CameraOrthographic orthographic;
    private final CameraPerspective perspective;
    private final CameraType type;

    public Camera(JSONObject json) {
        this.name = json.optString("name");
        this.type = CameraType.getById(json.optString("type"));

        if(this.type == CameraType.ORTHOGRAPHIC) {
            this.perspective = null;
            this.orthographic = new CameraOrthographic(json.getJSONObject("orthographic"));
        } else {
            this.perspective = new CameraPerspective(json.getJSONObject("perspective"));
            this.orthographic = null;
        }
    }

    public String getName() {
        return name;
    }

    public CameraOrthographic getOrthographic() {
        return orthographic;
    }

    public CameraPerspective getPerspective() {
        return perspective;
    }

    public CameraType getType() {
        return type;
    }
}