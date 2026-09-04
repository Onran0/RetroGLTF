package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.GLMeshPrimitive;
import io.github.onran0.retrogltf.Node;
import io.github.onran0.retrogltf.util.ShaderCompileException;
import io.github.onran0.retrogltf.util.ShaderCompiler;
import io.github.onran0.retrogltf.util.ShadersQualifier;
import io.github.onran0.retrogltf.util.ShadersStore;

import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

public class BuiltinShaderLoader {

    private static final String DEFAULT_VERTEX_SHADER_PATH = "/shaders/builtin/default.vsh";
    private static final String UNLIT_FRAGMENT_SHADER_PATH = "/shaders/builtin/unlit.fsh";
    private static final String SKIN_0_256_VERTEX_SHADER_PATH = "/shaders/builtin/skin_0_256.vsh";

    private static final Map<BuiltinVertexShaderType, String> VERTEX_SHADER_PATHS = new EnumMap<>(
            BuiltinVertexShaderType.class
    );

    private static final Map<BuiltinFragmentShaderType, String> FRAGMENT_SHADER_PATHS = new EnumMap<>(
            BuiltinFragmentShaderType.class
    );

    private static final ShadersStore store = new ShadersStore();

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

    private static StringBuilder readResource(String path) throws IOException {
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

        return result;
    }

    private static int loadProgram(
            BuiltinVertexShaderType vert,
            BuiltinFragmentShaderType frag,
            Node node, GLMeshPrimitive primitive

    ) throws ShaderCompileException, IOException {
        StringBuilder vertContent = readResource(VERTEX_SHADER_PATHS.get(vert));
        StringBuilder fragContent = readResource(FRAGMENT_SHADER_PATHS.get(frag));

        ShadersQualifier.defineNodePrimitiveVars(vertContent, node, primitive);
        ShadersQualifier.defineNodePrimitiveVars(fragContent, node, primitive);

        return ShaderCompiler.createProgram(
                vertContent,
                fragContent
        );
    }

    public static int getBuiltinProgram(
            BuiltinVertexShaderType vert,
            BuiltinFragmentShaderType frag,
            Node node, GLMeshPrimitive primitive
    ) {
        int hash = vert.ordinal() | frag.ordinal() << 16;

        Optional<Integer> optProgram = store.getTargetShader(
                hash, node, primitive
        );

        int program;

        if(!optProgram.isPresent()) {
            try {
                program = loadProgram(vert, frag, node, primitive);
            } catch(ShaderCompileException | IOException e) {
                throw new RuntimeException("failed to compile builtin shader", e);
            }

            store.cacheShader(hash, program, node, primitive);
        } else {
            program = optProgram.get();
        }

        return program;
    }

    public static void free() {
        store.free();
        store.clear();
    }
}