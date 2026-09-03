#version 420 core

layout(location = 0) in vec3 aPosition;
layout(location = 2) in vec2 aTexCoord0;
layout(location = 3) in vec2 aTexCoord1;

layout(location = 4) in ivec4 aJoints0;
layout(location = 5) in vec4 aWeights0;

layout(location = 0) out vec2 vTexCoord0;
layout(location = 1) out vec2 vTexCoord1;

uniform mat4 uMVPMatrix;

layout(std140, binding = 0) uniform SkinningBlock {
    mat4 u_skinMatrices[256];
};

void main() {
    vTexCoord0 = aTexCoord0;
    vTexCoord1 = aTexCoord1;

    mat4 skinMat =
    u_skinMatrices[aJoints0.x] * aWeights0.x +
    u_skinMatrices[aJoints0.y] * aWeights0.y +
    u_skinMatrices[aJoints0.z] * aWeights0.z +
    u_skinMatrices[aJoints0.w] * aWeights0.w;

    gl_Position = uMVPMatrix * (skinMat * vec4(aPosition, 1.0));
}