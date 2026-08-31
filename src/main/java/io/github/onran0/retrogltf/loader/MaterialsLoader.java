package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLTexture;
import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.loader.structure.material.GLTFMaterial;
import io.github.onran0.retrogltf.loader.structure.material.GLTFPBRMetallicRoughness;

class MaterialsLoader {
    private GLTFMaterial[] materials;
    private GLTexture[] textures;

    public MaterialsLoader(GLTFMaterial[] materials, GLTexture[] textures) {
        this.materials = materials;
        this.textures = textures;
    }

    public Material[] loadMaterials() {
        Material[] glMaterials = new Material[this.materials.length];

        for(int i = 0; i < this.materials.length; i++) {
            GLTFMaterial material = this.materials[i];

            // TODO: fully PBR support
            if(material != null) {
                GLTFPBRMetallicRoughness pbr = material.getPBRMetallicRoughness();

                GLTexture diffuseTexture;

                if(pbr.getBaseColorTexture().isPresent()) {
                    diffuseTexture = this.textures[pbr.getBaseColorTexture().get().getIndex()];
                } else {
                    diffuseTexture = GLTexture.MISSING;
                }

                glMaterials[i] = new Material(diffuseTexture, !material.isDoubleSided());
            }
        }

        return glMaterials;
    }
}