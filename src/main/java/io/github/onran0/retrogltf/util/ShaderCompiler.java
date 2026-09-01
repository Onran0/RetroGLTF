package io.github.onran0.retrogltf.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderCompiler {

    private static int compileShader(
            final String content,
            final int type
    ) throws ShaderCompileException {
        int shader = GL20.glCreateShader(type);

        GL20.glShaderSource(shader, content);
        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new ShaderCompileException(GL20.glGetShaderInfoLog(shader, 1024));
        }

        return shader;
    }

    public static int createProgram(
            final String vertexShaderContent,
            final String fragmentShaderContent
    ) throws ShaderCompileException {
        int program = GL20.glCreateProgram();

        if(vertexShaderContent != null)
            GL20.glAttachShader(program, compileShader(vertexShaderContent, GL20.GL_VERTEX_SHADER));

        if(fragmentShaderContent != null)
            GL20.glAttachShader(program, compileShader(fragmentShaderContent, GL20.GL_FRAGMENT_SHADER));

        GL20.glLinkProgram(program);
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            throw new ShaderCompileException(GL20.glGetProgramInfoLog(program, 1024));
        }

        GL20.glValidateProgram(program);

        return program;
    }
}