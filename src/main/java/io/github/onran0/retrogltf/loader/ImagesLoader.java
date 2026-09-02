package io.github.onran0.retrogltf.loader;

import io.github.onran0.retrogltf.loader.structure.texture.GLTFImage;
import io.github.onran0.retrogltf.loader.util.ByteBufferInputStream;
import io.github.onran0.retrogltf.loader.util.IOUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

class ImagesLoader {
    private ImagesLoader() { }

    public static BufferedImage[] loadImages(LoadContext context) throws GLTFLoadException {
        Profiler.startTaskTrack(LoaderTaskType.IMAGE_DECODING);

        BufferViewsReader viewsReader = context.getViewsReader();
        GLTFParser parser = context.getParser();
        GLTFImage[] images = context.getParser().getImages();

        ByteBuffer fastBuf = context.getFastBuffer();

        BufferedImage[] loadedImages = new BufferedImage[images.length];

        for(int i = 0; i < images.length; i++) {
            GLTFImage image = images[i];

            if(image != null) {
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
                } else throw new GLTFLoadException("invalid image by id " + i);

                imgBuf.rewind();

                try {
                    loadedImages[i] = ImageIO.read(new ByteBufferInputStream(imgBuf));
                } catch (IOException e) {
                    throw new GLTFLoadException(e);
                }
            }
        }

        Profiler.endTaskTrack();

        return loadedImages;
    }
}