#version 420 core

layout(location = POSITION) in vec3 aPosition;

#ifdef TEXCOORD_0
layout(location = TEXCOORD_0) in vec2 aTexCoord0;
layout(location = 0) out vec2 vTexCoord0;
#endif

#ifdef TEXCOORD_1
layout(location = TEXCOORD_1) in vec2 aTexCoord1;
layout(location = 1) out vec2 vTexCoord1;
#endif

#ifdef JOINTS_0
layout(location = JOINTS_0) in ivec4 aJoints0;
layout(location = WEIGHTS_0) in vec4 aWeights0;
#endif

uniform mat4 uMVPMatrix;

#ifdef HAS_SKIN
layout(std140, binding = 0) uniform SkinningBlock {
    mat4 u_skinMatrices[256];
};
#endif

void main() {
    #ifdef TEXCOORD_0
        vTexCoord0 = aTexCoord0;
    #endif

    #ifdef TEXCOORD_1
        vTexCoord1 = aTexCoord1;
    #endif

    #ifdef HAS_SKIN
        mat4 skinMat =
        u_skinMatrices[aJoints0.x] * aWeights0.x +
        u_skinMatrices[aJoints0.y] * aWeights0.y +
        u_skinMatrices[aJoints0.z] * aWeights0.z +
        u_skinMatrices[aJoints0.w] * aWeights0.w;

        gl_Position = uMVPMatrix * (skinMat * vec4(aPosition, 1.0));
    #else
        gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
    #endif
}