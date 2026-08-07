package io.github.onran0.retrogltf.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IOUtil {

    public static ByteBuffer channelToBuffer(FileChannel channel, String fileName) throws IOException {
        long fileSize = channel.size();

        if(fileSize > Integer.MAX_VALUE)
            throw new IOException(String.format("Size of file \"%s\" is bigger than Integer.MAX_VALUE", fileName));

        ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);

        channel.read(buffer);

        buffer.flip();

        channel.close();

        return buffer;
    }
}