package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.GLTexture;
import io.github.onran0.retrogltf.enums.*;
import io.github.onran0.retrogltf.loader.structure.texture.GLTFTexture;
import io.github.onran0.retrogltf.loader.structure.texture.GLTFTextureSampler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

class TexturesLoader {
    private static final ImageContainer MISSING_TEX;

    static {
        int purple = 0xFFFC03F4;

        BufferedImage bufImg = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);

        bufImg.setRGB(0, 0, purple);
        bufImg.setRGB(1, 1, purple);

        Profiler.setEnabledTrack(false);

        MISSING_TEX = ImagesDecoder.bufferedImageToRGBA8ImageContainer(
                bufImg, ByteBuffer.allocateDirect(4 * 4 * 4), false
        );

        Profiler.setEnabledTrack(true);
    }

    private TexturesLoader() { }

    public static GLTexture[] loadTextures(LoadContext context) throws GLTFLoadException {
        Profiler.startTaskTrack(LoaderTaskType.TEXTURE_LOADING);

        GLTFTexture[] textures = context.getParser().getTextures();
        GLTFTextureSampler[] samplers = context.getParser().getTextureSamplers();
        GLTFParser parser = context.getParser();

        ImageContainer[] cachedImages = new ImageContainer[parser.getImages().length];

        GLTexture[] glTextures = new GLTexture[textures.length];

        for(int i = 0; i < textures.length; i++) {
            GLTFTexture texture = textures[i];

            if(texture != null) {
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

                ImageContainer imgContainer;

                if(texture.getSource().isPresent()) {
                    int source = texture.getSource().get();

                    if(cachedImages[source] != null) {
                        imgContainer = cachedImages[source];
                        imgContainer.getBuffer().rewind();
                    } else {
                        imgContainer = ImagesDecoder.loadImage(context, source);

                        if(parser.getImageReferencesCount(source) > 1) {
                            if(imgContainer.isBufferFromPool()) {
                                ByteBuffer cacheBuffer = ByteBuffer.allocateDirect(
                                        imgContainer.getBuffer().limit()
                                );

                                cacheBuffer.put(imgContainer.getBuffer());
                                cacheBuffer.flip();

                                context.pushFastBuffer(imgContainer.getBuffer());

                                imgContainer = new ImageContainer(
                                        imgContainer.getWidth(),
                                        imgContainer.getHeight(),
                                        imgContainer.getColorModel(),
                                        cacheBuffer,
                                        false
                                );
                            }

                            cachedImages[source] = imgContainer;
                        }
                    }
                } else {
                    imgContainer = MISSING_TEX;
                    imgContainer.getBuffer().rewind();
                }

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

                if(
                        imgContainer.getColorModel() == ImageColorModel.G ||
                        imgContainer.getColorModel() == ImageColorModel.GA
                ) {
                    GL11.glTexParameteri(
                            GL11.GL_TEXTURE_2D,
                            GL33.GL_TEXTURE_SWIZZLE_R,
                            GL11.GL_RED
                    );

                    GL11.glTexParameteri(
                            GL11.GL_TEXTURE_2D,
                            GL33.GL_TEXTURE_SWIZZLE_G,
                            GL11.GL_RED
                    );

                    GL11.glTexParameteri(
                            GL11.GL_TEXTURE_2D,
                            GL33.GL_TEXTURE_SWIZZLE_B,
                            GL11.GL_RED
                    );

                    if(imgContainer.getColorModel() == ImageColorModel.GA) {
                        GL11.glTexParameteri(
                                GL11.GL_TEXTURE_2D,
                                GL33.GL_TEXTURE_SWIZZLE_A,
                                GL11.GL_GREEN
                        );
                    }
                }

                GL11.glTexImage2D(
                        GL11.GL_TEXTURE_2D, 0,
                        imgContainer.getColorModel().getInternalGLFormat(),
                        imgContainer.getWidth(), imgContainer.getHeight(),
                        0, imgContainer.getColorModel().getGLFormat(),
                        GL11.GL_UNSIGNED_BYTE, imgContainer.getBuffer()
                );

                if(minFilter.isMipmap()) {
                    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

                if(imgContainer.isBufferFromPool()) {
                    context.pushFastBuffer(imgContainer.getBuffer());
                }

                glTextures[i] = new GLTexture(textureId);
            }
        }

        Profiler.endTaskTrack();

        return glTextures;
    }
}