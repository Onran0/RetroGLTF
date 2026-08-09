package io.github.onran0.retrogltf.io;

import io.github.onran0.retrogltf.util.IOUtil;

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

    @Override
    public ByteBuffer getFileData(String path, int length) throws IOException {
        Path file = root.resolve(path);

        return IOUtil.channelToBuffer(FileChannel.open(file, StandardOpenOption.READ), length);
    }
}