package io.github.onran0.retrogltf.util;

import io.github.onran0.retrogltf.GLMeshPrimitive;
import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.enums.PrimitiveAttributeType;

import java.util.List;
import java.util.Map;

public class ShadersQualifier {

    private static int getVersionEndIndex(StringBuilder src) {
        int idx = src.indexOf("#version");

        while(src.charAt(idx) != '\n' && src.length() > idx) {
            idx++;
        }

        return idx + 1;
    }

    public static void define(StringBuilder src, String def) {
        int idx = getVersionEndIndex(src);

        src.insert(idx, '\n');
        src.insert(idx, def);
        src.insert(idx, "#define ");
    }

    public static void define(StringBuilder src, String def, Object val) {
        int idx = getVersionEndIndex(src);

        src.insert(idx, '\n');
        src.insert(idx, val);
        src.insert(idx, ' ');
        src.insert(idx, def);
        src.insert(idx, "#define ");
    }

    public static void defineNodePrimitiveVars(StringBuilder src, Node node, GLMeshPrimitive primitive) {
        if(node.getSkin().isPresent()) {
            define(src, "HAS_SKIN");
        }

        Material material = node.getMaterial(primitive.getMaterialIndex());

        if(material != null) {
            define(src, "HAS_MATERIAL");

            if(material.getBaseColor() != null) {
                define(src, "HAS_BASE_COLOR");
                define(src, "BASE_COLOR_TEX_COORD_INDEX", material.getBaseColor().getTexCoordIndex());
            }
        }

        defineAttributeLocations(src, primitive);
    }

    public static void defineAttributeLocations(StringBuilder src, GLMeshPrimitive primitive) {
        defineAttributeLocations(
                src,
                primitive.getAttributeLocations(),
                primitive.getMorphTargetsAttributeLocations()
        );
    }

    public static void defineAttributeLocations(
            StringBuilder src,
            Map<PrimitiveAttributeType, Integer> attributeLocations,
            List<Map<PrimitiveAttributeType, Integer>> morphTargetsAttributeLocations
    ) {
        defineAttributeLocations(src, attributeLocations, "");

        if(morphTargetsAttributeLocations != null) {
            define(src, "HAS_MORPHS");
            define(src, "MORPHS_COUNT", morphTargetsAttributeLocations.size());

            for(int i = 0;i < morphTargetsAttributeLocations.size();i++) {
                Map<PrimitiveAttributeType, Integer> morphTargetAttributeLocations =
                        morphTargetsAttributeLocations.get(i);

                defineAttributeLocations(
                        src,
                        morphTargetAttributeLocations,
                        "MORPH_" + i + "_"
                );
            }
        }
    }

    public static void defineAttributeLocations(
            StringBuilder src,
            Map<PrimitiveAttributeType, Integer> attributeLocations,
            String definesPrefix
    ) {
        for(PrimitiveAttributeType attributeType : attributeLocations.keySet()) {
            define(
                    src,
                    definesPrefix + attributeType.getId(),
                    attributeLocations.get(attributeType)
            );
        }
    }
}