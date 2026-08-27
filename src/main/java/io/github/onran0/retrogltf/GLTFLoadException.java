package io.github.onran0.retrogltf;

public class GLTFLoadException extends Exception {

    public GLTFLoadException(final String message) {
        super(message);
    }

    public GLTFLoadException(final Throwable cause) {
        super(cause);
    }
}