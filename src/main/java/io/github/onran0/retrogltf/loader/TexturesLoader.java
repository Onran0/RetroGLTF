package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLTexture;
import io.github.onran0.retrogltf.enums.*;
import io.github.onran0.retrogltf.loader.structure.texture.GLTFTexture;
import io.github.onran0.retrogltf.loader.structure.texture.GLTFTextureSampler;
import io.github.onran0.retrogltf.loader.util.IOUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

class TexturesLoader {
    private static final BufferedImage MISSING_TEX = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);

    static {
        int purple = 0xF403FCFF;

        MISSING_TEX.setRGB(0, 0, purple);
        MISSING_TEX.setRGB(1, 1, purple);
    }

    private TexturesLoader() { }

    public static GLTexture[] loadTextures(LoadContext context, BufferedImage[] images) {
        GLTFTexture[] textures = context.getParser().getTextures();
        GLTFTextureSampler[] samplers = context.getParser().getTextureSamplers();

        ByteBuffer fastBuf = context.getFastBuffer();

        GLTexture[] glTextures = new GLTexture[textures.length];

        for(int i = 0; i < textures.length; i++) {
            GLTFTexture texture = textures[i];

            if(texture != null) {
                BufferedImage sourceImg;

                if(texture.getSource().isPresent()) {
                    sourceImg = images[texture.getSource().get()];
                } else {
                    sourceImg = MISSING_TEX;
                }

                TextureMagFilter magFilter;
                TextureMinFilter minFilter;

                TextureWrapMode wrapS;
                TextureWrapMode wrapT;

                if(texture.getSampler().isPresent()) {
                    GLTFTextureSampler sampler = samplers[texture.getSampler().get()];

                    magFilter = sampler.getMagFilter().orElse(TextureMagFilter.LINEAR);
                    minFilter = sampler.getMinFilter().orElse(TextureMinFilter.LINEAR_MIPMAP_LINEAR);

                    wrapS = sampler.getWrapS();
                    wrapT = sampler.getWrapT();
                } else {
                    magFilter = TextureMagFilter.LINEAR;
                    minFilter = TextureMinFilter.LINEAR;

                    wrapS = TextureWrapMode.REPEAT;
                    wrapT = TextureWrapMode.REPEAT;
                }

                int width = sourceImg.getWidth();
                int height = sourceImg.getHeight();

                int[] rgbArray = new int[width * height];

                sourceImg.getRGB(0, 0, width, height, rgbArray, 0, width);

                int pixelsLength = width * height * 4;
                ByteBuffer pixels = IOUtil.getFastOrDirectAlloc(fastBuf, pixelsLength);

                for (int y = 0; y < height; y++) {
                    int rowOffset = y * width;

                    for (int x = 0; x < width; x++) {
                        int color = rgbArray[rowOffset + x];

                        pixels.put((byte) ((color >> 16) & 0xFF)); // R
                        pixels.put((byte) ((color >> 8) & 0xFF));  // G
                        pixels.put((byte) (color & 0xFF));         // B
                        pixels.put((byte) ((color >> 24) & 0xFF)); // A
                    }
                }

                pixels.flip();

                int textureId = GL11.glGenTextures();

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

                GL11.glTexParameteri(
                        GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MAG_FILTER,
                        magFilter.getGLType()
                );

                GL11.glTexParameteri(
                        GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER,
                        minFilter.getGLType()
                );

                GL11.glTexParameteri(
                        GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_WRAP_S,
                        wrapS.getGLType()
                );

                GL11.glTexParameteri(
                        GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_WRAP_T,
                        wrapT.getGLType()
                );

                GL11.glTexImage2D(
                        GL11.GL_TEXTURE_2D, 0,
                        GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA,
                        GL11.GL_UNSIGNED_BYTE, pixels
                );

                //GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

                glTextures[i] = new GLTexture(textureId);
            }
        }

        return glTextures;
    }
}