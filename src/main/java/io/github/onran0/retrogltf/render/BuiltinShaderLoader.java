package io.github.onran0.retrogltf.render;

import io.github.onran0.retrogltf.util.ShaderCompileException;
import io.github.onran0.retrogltf.util.ShaderCompiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class BuiltinShaderLoader {

    private static final String VERTEX_SHADER_PATH = "/shaders/builtin.vsh";
    private static final String FRAGMENT_SHADER_PATH = "/shaders/builtin.fsh";

    private static int program;

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

    private static int loadProgram() throws ShaderCompileException, IOException {
        return ShaderCompiler.createProgram(
                readResource(VERTEX_SHADER_PATH),
                readResource(FRAGMENT_SHADER_PATH)
        );
    }

    public static int getBuiltinProgram() {
        if(program == 0) {
            try {
                program = loadProgram();
            } catch(ShaderCompileException | IOException e) {
                throw new RuntimeException("failed to load builtin shader", e);
            }
        }

        return program;
    }
}