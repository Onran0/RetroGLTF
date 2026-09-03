package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.util.ShaderCompileException;
import io.github.onran0.retrogltf.util.ShaderCompiler;

import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

class BuiltinShaderLoader {

    private static final String DEFAULT_VERTEX_SHADER_PATH = "/shaders/builtin/default.vsh";
    private static final String UNLIT_FRAGMENT_SHADER_PATH = "/shaders/builtin/unlit.fsh";
    private static final String SKIN_0_256_VERTEX_SHADER_PATH = "/shaders/builtin/skin/skin_0_256.vsh";

    private static final Map<BuiltinVertexShaderType, String> VERTEX_SHADER_PATHS = new EnumMap<>(
            BuiltinVertexShaderType.class
    );

    private static final Map<BuiltinFragmentShaderType, String> FRAGMENT_SHADER_PATHS = new EnumMap<>(
            BuiltinFragmentShaderType.class
    );

    private static final Map<Integer, Integer> loadedPrograms = new HashMap<>();

    static {
        VERTEX_SHADER_PATHS.put(
                BuiltinVertexShaderType.DEFAULT,
                DEFAULT_VERTEX_SHADER_PATH
        );

        VERTEX_SHADER_PATHS.put(
                BuiltinVertexShaderType.SKIN_0_256,
                SKIN_0_256_VERTEX_SHADER_PATH
        );

        FRAGMENT_SHADER_PATHS.put(
                BuiltinFragmentShaderType.UNLIT,
                UNLIT_FRAGMENT_SHADER_PATH
        );
    }

    private static String readResource(String path) throws IOException {
        InputStream is = BuiltinShaderLoader.class.getResourceAsStream(path);

        if(is == null)
            throw new IllegalArgumentException("unknown resource: " + path);

        StringBuilder result = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line;

        while((line = reader.readLine()) != null)
            result.append(line).append("\n");

        reader.close();

        return result.toString();
    }

    private static int loadProgram(
            BuiltinVertexShaderType vert,
            BuiltinFragmentShaderType frag

    ) throws ShaderCompileException, IOException {

        return ShaderCompiler.createProgram(
                readResource(VERTEX_SHADER_PATHS.get(vert)),
                readResource(FRAGMENT_SHADER_PATHS.get(frag))
        );
    }

    public static int getBuiltinProgram(
            BuiltinVertexShaderType vert,
            BuiltinFragmentShaderType frag
    ) {
        int hash = vert.ordinal() | frag.ordinal() << 16;

        if(!loadedPrograms.containsKey(hash)) {
            try {
                loadedPrograms.put(hash, loadProgram(vert, frag));
            } catch(ShaderCompileException | IOException e) {
                throw new RuntimeException("failed to load builtin shader", e);
            }
        }

        return loadedPrograms.get(hash);
    }
}