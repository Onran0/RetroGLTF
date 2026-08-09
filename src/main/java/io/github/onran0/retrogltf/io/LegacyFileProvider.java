package io.github.onran0.retrogltf.io;

import io.github.onran0.retrogltf.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class LegacyFileProvider implements IFileProvider {

    private final File root;

    public LegacyFileProvider(File root) {
        this.root = root;
    }

    private ByteBuffer rawGetFileData(String path, Integer length) throws IOException {
        File file = new File(root, path);

        FileInputStream fis = new FileInputStream(file);
        FileChannel fileChannel = fis.getChannel();

        return IOUtil.channelToBuffer(fileChannel, length);
    }

    @Override
    public ByteBuffer getFileData(String path) throws IOException {
        return rawGetFileData(path, null);
    }

    @Override
    public ByteBuffer getFileData(String path, int length) throws IOException {
        return rawGetFileData(path, length);
    }
}