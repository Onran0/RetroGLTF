package io.github.onran0.retrogltf.io;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface IFileProvider {

    ByteBuffer getFileData(String path) throws IOException;

    ByteBuffer getFileData(String path, int length) throws IOException;
}