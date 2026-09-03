package io.github.onran0.retrogltf.loader.structure.mesh;

import io.github.onran0.retrogltf.enums.ElementsType;
import io.github.onran0.retrogltf.enums.PrimitiveAttributeType;
import io.github.onran0.retrogltf.loader.util.JSONUtil;
import org.json.JSONObject;

import java.util.Optional;
import java.util.Set;

public class GLTFMeshPrimitive {

    public static class Attribute {
        private final String type;
        private final int accessor;

        private final PrimitiveAttributeType regularType;

        public Attribute(String type, int accessor) {
            this.type = type;
            this.accessor = accessor;

            this.regularType = PrimitiveAttributeType.getById(type);
        }

        public String getType() {
            return type;
        }

        public PrimitiveAttributeType getRegularType() {
            return regularType;
        }

        public int getAccessor() {
            return accessor;
        }
    }

    private final Attribute[] attributes;
    private final Integer indices;
    private final Integer material;
    private final ElementsType mode;

    public GLTFMeshPrimitive(JSONObject json) {
        JSONObject attributes = json.getJSONObject("attributes");

        Set<String> attrTypes = attributes.keySet();

        this.attributes = new Attribute[attrTypes.size()];

        int i = 0;

        for(String key : attrTypes) {
            this.attributes[i] = new Attribute(key, attributes.getInt(key));

            i++;
        }

        this.indices = JSONUtil.getNullableInt(json, "indices");
        this.material = JSONUtil.getNullableInt(json, "material");
        this.mode = ElementsType.getById(json.optInt("mode", 4));
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public Optional<Integer> getIndices() {
        return Optional.ofNullable(indices);
    }

    public Optional<Integer> getMaterial() {
        return Optional.ofNullable(material);
    }

    public ElementsType getMode() {
        return mode;
    }
}