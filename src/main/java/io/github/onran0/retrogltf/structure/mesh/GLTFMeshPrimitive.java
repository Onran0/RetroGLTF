package io.github.onran0.retrogltf.structure.mesh;

import io.github.onran0.retrogltf.constants.PrimitiveAttributeTypes;
import io.github.onran0.retrogltf.util.JSONUtil;
import org.json.JSONObject;

import java.util.Set;

public class GLTFMeshPrimitive {
    public enum Mode {
        POINTS(0),
        LINE_STRIPS(1),
        LINE_LOOPS(2),
        LINES(3),
        TRIANGLES(4),
        TRIANGLE_STRIPS(5),
        TRIANGLE_FANS(6);

        private final int id;

        Mode(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Mode getById(int id) {
            for(Mode type : Mode.values()) {
                if(type.getId() == id)
                    return type;
            }

            return null;
        }
    }

    public static class Attribute {
        private final String type;
        private final int accessor;

        private final PrimitiveAttributeTypes regularType;

        public Attribute(String type, int accessor) {
            this.type = type;
            this.accessor = accessor;

            this.regularType = PrimitiveAttributeTypes.getById(type);
        }

        public String getType() {
            return type;
        }

        public PrimitiveAttributeTypes getRegularType() {
            return regularType;
        }

        public int getAccessor() {
            return accessor;
        }
    }

    private final Attribute[] attributes;
    private final Integer indices;
    private final Integer material;
    private final Mode mode;

    public GLTFMeshPrimitive(JSONObject json) {
        JSONObject attributes = json.getJSONObject("attributes");

        Set<String> attrTypes = attributes.keySet();

        this.attributes = new Attribute[attrTypes.size()];

        int i = 0;

        for(String key : attrTypes) {
            this.attributes[i] = new Attribute(key, attributes.getInt(key));

            i++;
        }

        this.indices = JSONUtil.getNullableInt(attributes, "indices");
        this.material = JSONUtil.getNullableInt(attributes, "material");
        this.mode = Mode.getById(json.optInt("mode", 4));
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public boolean hasIndices() {
        return indices != null;
    }

    public int getIndices() {
        return indices;
    }

    public boolean hasMaterial() {
        return material != null;
    }

    public int getMaterial() {
        return material;
    }

    public Mode getMode() {
        return mode;
    }
}