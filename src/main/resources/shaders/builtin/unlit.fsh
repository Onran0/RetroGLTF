#version 430 core

#ifdef TEXCOORD_0
layout(location = 0) in vec2 vTexCoord0;
#endif

#ifdef TEXCOORD_1
layout(location = 1) in vec2 vTexCoord1;
#endif

layout(location = 0) out vec4 fragColor;

#ifdef HAS_BASE_COLOR
layout(binding = 0) uniform sampler2D uBaseColor;
#endif

void main() {
    #ifdef HAS_BASE_COLOR
        #if BASE_COLOR_TEX_COORD_INDEX == 0
            fragColor = texture(uBaseColor, vTexCoord0);
        #else
            fragColor = texture(uBaseColor, vTexCoord1);
        #endif
    #else
        fragColor = vec4(1.0);
    #endif
}