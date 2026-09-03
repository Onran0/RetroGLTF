package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.enums.MimeType;
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

    public static ImageContainer bufferedImageToRGBA8ImageContainer(BufferedImage image, ByteBuffer cachedBuf, boolean isFromPool) {
        int width = image.getWidth();
        int height = image.getHeight();

        Profiler.startTaskTrack(LoaderTaskType.AWT_IMAGE_TO_BUFFER_TRANSCODING);

        int[] rgbArray = new int[width * height];

        image.getRGB(0, 0, width, height, rgbArray, 0, width);

        ByteBuffer imgBuf = IOUtil.getFastOrDirectAlloc(cachedBuf,width * height * 4);

        boolean hasAlpha = image.getColorModel().hasAlpha();

        for (int y = 0; y < height; y++) {
            int rowOffset = y * width;

            for (int x = 0; x < width; x++) {
                int color = rgbArray[rowOffset + x];

                imgBuf.put((byte) ((color >> 16) & 0xFF)); // R
                imgBuf.put((byte) ((color >> 8) & 0xFF));  // G
                imgBuf.put((byte) (color & 0xFF));         // B

                if(hasAlpha) {
                    imgBuf.put((byte) ((color >> 24) & 0xFF));
                }
            }
        }

        imgBuf.flip();

        Profiler.endTaskTrack();

        return new ImageContainer(
                width, height, hasAlpha ? ImageColorModel.RGBA : ImageColorModel.RGB,
                imgBuf, imgBuf == cachedBuf && isFromPool
        );
    }

    public static ImageContainer loadImage(LoadContext context, int id) throws GLTFLoadException {
        Profiler.startTaskTrack(LoaderTaskType.IMAGE_DECODING);

        BufferViewsReader viewsReader = context.getViewsReader();
        GLTFParser parser = context.getParser();

        GLTFImage image = parser.getImages()[id];

        ByteBuffer fastBuf = null;

        ByteBuffer imgBuf;

        if(image.getBufferView().isPresent()) {
            int view = image.getBufferView().get();
            int len = viewsReader.getViewLength(view);

            imgBuf = IOUtil.getFastOrDirectAlloc(fastBuf = context.popFastBuffer(), len);

            if(imgBuf != fastBuf)
                context.pushFastBuffer(fastBuf);

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
            MimeType mimeType = MimeType.getMimeType(imgBuf);

            ImageContainer res;

            switch(mimeType) {
                case PNG:
                    res = PNGJImageDecoder.decodePng(context, imgBuf);

                    if(imgBuf == fastBuf)
                        context.pushFastBuffer(fastBuf);

                    break;

                case JPEG:
                    BufferedImage bufImg = ImageIO.read(new ByteBufferInputStream(imgBuf));

                    imgBuf.clear();

                    res = bufferedImageToRGBA8ImageContainer(bufImg, imgBuf, imgBuf == fastBuf);

                    if(!res.isBufferFromPool() && imgBuf == fastBuf)
                        context.pushFastBuffer(fastBuf);

                    break;

                default: throw new GLTFLoadException("unsupported mime type: " + mimeType);
            }

            Profiler.endTaskTrack();

            return res;
        } catch (IOException e) {
            throw new GLTFLoadException(e);
        }
    }
}