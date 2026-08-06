package io.github.onran0.retrogltf;

import org.json.JSONObject;

public class GLTFParser {

    private JSONObject root;

    public GLTFParser(JSONObject root) {
        this.root = root;
    }

    public void parse() {
        for(Object buffer : root.getJSONArray("buffers")) {
            JSONObject bufferJson = (JSONObject) buffer;


        }
    }
}