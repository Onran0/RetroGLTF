package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.loader.structure.texture.GLTFImage;
import io.github.onran0.retrogltf.loader.util.ByteBufferInputStream;
import io.github.onran0.retrogltf.loader.util.IOUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

class ImagesDecoder {
    private ImagesDecoder() { }

    static {
        ImageIO.setUseCache(false);
    }

    public static RGBA8ImageContainer bufferedImageToRGBA8ImageContainer(BufferedImage image, ByteBuffer fastBuf) {
        int width = image.getWidth();
        int height = image.getHeight();

        Profiler.startTaskTrack(LoaderTaskType.AWT_IMAGE_TO_BUFFER_TRANSCODING);

        int[] rgbArray = new int[width * height];

        image.getRGB(0, 0, width, height, rgbArray, 0, width);

        ByteBuffer imgBuf = IOUtil.getFastOrDirectAlloc(fastBuf,width * height * 4);

        for (int y = 0; y < height; y++) {
            int rowOffset = y * width;

            for (int x = 0; x < width; x++) {
                int color = rgbArray[rowOffset + x];

                imgBuf.put((byte) ((color >> 16) & 0xFF)); // R
                imgBuf.put((byte) ((color >> 8) & 0xFF));  // G
                imgBuf.put((byte) (color & 0xFF));         // B
                imgBuf.put((byte) ((color >> 24) & 0xFF)); // A
            }
        }

        imgBuf.flip();

        Profiler.endTaskTrack();

        return new RGBA8ImageContainer(
                width, height, imgBuf, imgBuf == fastBuf
        );
    }

    public static RGBA8ImageContainer loadImage(LoadContext context, int id) throws GLTFLoadException {
        Profiler.startTaskTrack(LoaderTaskType.IMAGE_DECODING);

        BufferViewsReader viewsReader = context.getViewsReader();
        GLTFParser parser = context.getParser();

        GLTFImage image = parser.getImages()[id];

        ByteBuffer fastBuf = context.getFastBuffer();

        ByteBuffer imgBuf;

        if(image.getBufferView().isPresent()) {
            int view = image.getBufferView().get();
            int len = viewsReader.getViewLength(view);

            imgBuf = IOUtil.getFastOrDirectAlloc(fastBuf, len);

            viewsReader.get(imgBuf, view, 0, len);
        } else if(image.getURI().isPresent()) {
            try {
                imgBuf = parser.getBuffer(image.getURI().get(), -1);
            } catch (IOException e) {
                throw new GLTFLoadException(e);
            }
        } else throw new GLTFLoadException("invalid image by id " + id);

        imgBuf.rewind();

        try {
            BufferedImage bufImg = ImageIO.read(new ByteBufferInputStream(imgBuf));

            Profiler.endTaskTrack();

            imgBuf.clear();

            return bufferedImageToRGBA8ImageContainer(bufImg, imgBuf);
        } catch (IOException e) {
            throw new GLTFLoadException(e);
        }
    }
}