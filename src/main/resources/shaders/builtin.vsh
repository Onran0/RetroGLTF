#version 330 core

layout(location = 0) in vec3 aPosition;
layout(location = 2) in vec2 aTexCoord0;
layout(location = 3) in vec2 aTexCoord1;

layout(location = 0) out vec2 vTexCoord0;
layout(location = 1) out vec2 vTexCoord1;

uniform mat4 uMatrix;

void main() {
    vTexCoord0 = aTexCoord0;
    vTexCoord1 = aTexCoord1;

    gl_Position = uMatrix * vec4(aPosition, 1.0);
}