package io.github.onran0.retrogltf.loader.util;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.util.Arrays;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class IOUtil {

    private static final Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");

            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            f.setAccessible(false);
        } catch (Exception e) {
            throw new RuntimeException("failed to get unsafe", e);
        }
    }

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
            throw new IOException("required buffer length is bigger than Integer.MAX_VALUE");

        ByteBuffer buffer = ByteBuffer.allocate((int) toRead);

        channel.read(buffer);

        buffer.flip();

        channel.close();

        return buffer;
    }

    public static ByteBuffer getFastOrDirectAlloc(ByteBuffer fastBuf, int requiredLen) {
        if(fastBuf.capacity() >= requiredLen) {
            fastBuf.position(0);
            return fastBuf;
        } else {
            return ByteBuffer.allocateDirect(requiredLen);
        }
    }

    public static ByteBuffer getFastOrAlloc(ByteBuffer fastBuf, int requiredLen) {
        if(fastBuf.capacity() >= requiredLen) {
            fastBuf.position(0);
            return fastBuf;
        } else {
            return ByteBuffer.allocate(requiredLen);
        }
    }

    public static void fillBufferWithZeros(ByteBuffer buf, int length) {
        if (length <= 0) return;

        if (buf.position() + length > buf.capacity()) {
            throw new IndexOutOfBoundsException("buffer capacity is not enough for passed length");
        }

        if (buf.isDirect()) {
            long baseAddress = ((sun.nio.ch.DirectBuffer) buf).address();
            int pos = buf.position();

            unsafe.setMemory(baseAddress + pos, length, (byte) 0);
        } else {
            byte[] array = buf.array();
            int offset = buf.arrayOffset() + buf.position();

            Arrays.fill(array, offset, offset + length, (byte) 0);
        }
    }
}