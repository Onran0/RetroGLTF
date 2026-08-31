package io.github.onran0.retrogltf.loader.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
    private final ByteBuffer buf;

    private int markedPosition = -1;

    public ByteBufferInputStream(final ByteBuffer buf) {
        this.buf = buf.duplicate();
    }

    @Override
    public int available() {
        return buf.remaining();
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readlimit) {
        markedPosition = buf.position();
    }

    @Override
    public void reset() throws IOException {
        if(markedPosition == -1)
            throw new IOException("invalid mark");

        buf.position(markedPosition);
    }

    @Override
    public int read(byte[] arr, int off, int len) {
        if (len == 0) {
            return 0;
        }

        if (!buf.hasRemaining()) {
            return -1;
        }

        int toRead = Math.min(buf.remaining(), len);

        buf.get(arr, off, toRead);

        return toRead;
    }

    @Override
    public int read() {
        if(!buf.hasRemaining()) {
            return -1;
        }

        return buf.get() & 0xFF;
    }

    @Override
    public long skip(long n) {
        if (n <= 0) {
            return 0;
        }

        int toSkip = Math.min(buf.remaining(), (int) n);

        buf.position(buf.position() + toSkip);

        return toSkip;
    }
}