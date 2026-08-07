package io.github.onran0.retrogltf.structure.mesh;

import io.github.onran0.retrogltf.constants.PrimitiveAttributeTypes;
import org.json.JSONObject;

import java.util.Set;

public class MeshPrimitive {
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
    private final int indices;
    private final int material;
    private final Mode mode;

    public MeshPrimitive(JSONObject json) {
        JSONObject attributes = json.getJSONObject("attributes");

        Set<String> attrTypes = attributes.keySet();

        this.attributes = new Attribute[attrTypes.size()];

        int i = 0;

        for(String key : attrTypes) {
            this.attributes[i] = new Attribute(key, attributes.getInt(key));

            i++;
        }

        this.indices = json.getInt("indices");
        this.material = json.getInt("material");
        this.mode = Mode.getById(json.getInt("mode"));
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public int getIndices() {
        return indices;
    }

    public int getMaterial() {
        return material;
    }

    public Mode getMode() {
        return mode;
    }
}