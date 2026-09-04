package io.github.onran0.retrogltf.loader;

public class LoadSettings {

    private boolean compileLibraryBuiltinShaders = true;

    public LoadSettings compileLibraryBuiltinShaders(boolean compileLibraryBuiltinShaders) {
        this.compileLibraryBuiltinShaders = compileLibraryBuiltinShaders;
        return this;
    }

    public boolean shouldCompileLibraryBuiltinShaders() {
        return compileLibraryBuiltinShaders;
    }
}