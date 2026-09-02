package io.github.onran0.retrogltf.loader;

import ar.com.hjg.pngj.IImageLine;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import io.github.onran0.retrogltf.loader.util.ByteBufferInputStream;
import io.github.onran0.retrogltf.loader.util.IOUtil;

import java.nio.ByteBuffer;

class PNGJImageDecoder {

    public static RGBA8ImageContainer decodePng(LoadContext context, ByteBuffer data) {
        PngReader reader = new PngReader(new ByteBufferInputStream(data));

        int width = reader.imgInfo.cols;
        int height = reader.imgInfo.rows;

        int channels = reader.imgInfo.channels;

        ByteBuffer fastBuf = context.popFastBuffer();

        ByteBuffer pixelsBuf = IOUtil.getFastOrDirectAlloc(fastBuf, width * height * channels);

        for (int row = 0; row < reader.imgInfo.rows; row++) {
            IImageLine l1 = reader.readRow();

            int[] scanline = ((ImageLineInt) l1).getScanline();

            for (int j = 0; j < reader.imgInfo.cols; j++) {
                pixelsBuf.put((byte) scanline[j * channels]); // R
                pixelsBuf.put((byte) scanline[j * channels + 1]); // G
                pixelsBuf.put((byte) scanline[j * channels + 2]); // B

                if(channels > 3)
                    pixelsBuf.put((byte) scanline[j * channels + 3]); // A
            }
        }

        reader.end();

        pixelsBuf.flip();

        return new RGBA8ImageContainer(
                width, height, channels < 4 ? ImageColorModel.RGB : ImageColorModel.RGBA,
                pixelsBuf, pixelsBuf == fastBuf
        );
    }
}