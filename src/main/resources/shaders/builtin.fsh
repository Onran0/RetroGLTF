#version 430 core

layout(location = 0) in vec2 vTexCoord0;
layout(location = 1) in vec2 vTexCoord1;
layout(location = 0) out vec4 fragColor;

layout(binding = 0) uniform sampler2D uBaseColor;

uniform int uBaseColorTexCoordIndex;

void main() {
    vec4 texColor = texture(uBaseColor, uBaseColorTexCoordIndex == 0 ? vTexCoord0 : vTexCoord1);

    fragColor = texColor;
}
