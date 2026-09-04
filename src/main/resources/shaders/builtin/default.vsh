#version 330 core

layout(location = POSITION) in vec3 aPosition;

#ifdef TEXCOORD_0
layout(location = TEXCOORD_0) in vec2 aTexCoord0;
layout(location = 0) out vec2 vTexCoord0;
#endif

#ifdef TEXCOORD_1
layout(location = TEXCOORD_1) in vec2 aTexCoord1;
layout(location = 1) out vec2 vTexCoord1;
#endif

uniform mat4 uMVPMatrix;

void main() {
    #ifdef TEXCOORD_0
    vTexCoord0 = aTexCoord0;
    #endif

    #ifdef TEXCOORD_1
    vTexCoord1 = aTexCoord1;
    #endif

    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
}