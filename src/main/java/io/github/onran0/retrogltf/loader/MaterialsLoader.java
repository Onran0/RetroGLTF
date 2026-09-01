package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLTexture;
import io.github.onran0.retrogltf.Material;
import io.github.onran0.retrogltf.TextureInfo;
import io.github.onran0.retrogltf.loader.structure.material.GLTFMaterial;
import io.github.onran0.retrogltf.loader.structure.material.GLTFPBRMetallicRoughness;
import io.github.onran0.retrogltf.loader.structure.texture.GLTFTextureInfo;

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
                int diffuseTexCoordIndex;

                if(pbr.getBaseColorTexture().isPresent()) {
                    GLTFTextureInfo texInfo = pbr.getBaseColorTexture().get();

                    diffuseTexture = textures[texInfo.getIndex()];
                    diffuseTexCoordIndex = texInfo.getIndex();
                } else {
                    diffuseTexture = GLTexture.MISSING;
                    diffuseTexCoordIndex = 0;
                }

                glMaterials[i] = new Material(
                        new TextureInfo(diffuseTexture, diffuseTexCoordIndex),
                        !material.isDoubleSided()
                );
            }
        }

        return glMaterials;
    }
}