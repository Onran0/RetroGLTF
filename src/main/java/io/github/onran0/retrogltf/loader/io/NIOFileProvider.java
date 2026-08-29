package io.github.onran0.retrogltf.loader.io;

import io.github.onran0.retrogltf.loader.util.IOUtil;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class NIOFileProvider implements IFileProvider {

    private final Path root;

    public NIOFileProvider(Path root) {
        this.root = root;
    }

    private ByteBuffer rawGetFileData(String path, Integer length) throws IOException {
        Path file = root.resolve(path);

        return IOUtil.channelToBuffer(FileChannel.open(file, StandardOpenOption.READ), length);
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