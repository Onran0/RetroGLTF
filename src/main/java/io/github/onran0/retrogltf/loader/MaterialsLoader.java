package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLTexture;
import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.loader.structure.material.GLTFMaterial;
import io.github.onran0.retrogltf.loader.structure.material.GLTFPBRMetallicRoughness;

class MaterialsLoader {
    private MaterialsLoader() { }

    public static Material[] loadMaterials(LoadContext context, GLTexture[] textures) {
        GLTFMaterial[] materials = context.getParser().getMaterials();

        Material[] glMaterials = new Material[materials.length];

        for(int i = 0; i < materials.length; i++) {
            GLTFMaterial material = materials[i];

            // TODO: fully PBR support
            if(material != null) {
                GLTFPBRMetallicRoughness pbr = material.getPBRMetallicRoughness();

                GLTexture diffuseTexture;

                if(pbr.getBaseColorTexture().isPresent()) {
                    diffuseTexture = textures[pbr.getBaseColorTexture().get().getIndex()];
                } else {
                    diffuseTexture = GLTexture.MISSING;
                }

                glMaterials[i] = new Material(diffuseTexture, !material.isDoubleSided());
            }
        }

        return glMaterials;
    }
}