package io.github.onran0.retrogltf.enums;

import java.nio.ByteBuffer;

public enum MimeType {
    PNG(new byte[] {
            (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47,
            (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A
    }),

    JPEG(new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF });

    private final byte[] magic;

    MimeType(byte[] magic) {
        this.magic = magic;
    }

    public void getMagic(byte[] dst) {
        System.arraycopy(magic, 0, dst, 0, magic.length);
    }

    public int getMagicLength() {
        return magic.length;
    }

    public static MimeType getMimeType(ByteBuffer buf) {
        MimeType res = null;

        for(MimeType mimeType : MimeType.values()) {
            byte[] magic = mimeType.magic;

            if(buf.remaining() < magic.length) {
                continue;
            }

            buf.mark();

            boolean equals = true;

            for (byte b : magic) {
                if (b != buf.get()) {
                    equals = false;
                    break;
                }
            }

            buf.reset();

            if(equals) {
                res = mimeType;
                break;
            }
        }

        return res;
    }
}