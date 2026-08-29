package io.github.onran0.retrogltf.loader.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IOUtil {

    public static ByteBuffer channelToBuffer(FileChannel channel) throws IOException {
        return channelToBuffer(channel, null);
    }

    public static ByteBuffer channelToBuffer(FileChannel channel, Integer length) throws IOException {
        long toRead;

        if(length != null)
            toRead = length;
        else
            toRead = channel.size();

        if(toRead > Integer.MAX_VALUE)
            throw new IOException("Required buffer length is bigger than Integer.MAX_VALUE");

        ByteBuffer buffer = ByteBuffer.allocate((int) toRead);

        channel.read(buffer);

        buffer.flip();

        channel.close();

        return buffer;
    }
}