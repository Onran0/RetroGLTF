package io.github.onran0.retrogltf.structure.asset;

import org.json.JSONObject;

public class Asset {
    private final String copyright;
    private final String generator;
    private final String version;
    private final String minVersion;

    public Asset(JSONObject json) {
        this.copyright = json.optString("copyright");
        this.generator = json.optString("generator");
        this.version = json.getString("version");
        this.minVersion = json.optString("minVersion");
    }

    public String getCopyright() {
        return copyright;
    }

    public String getGenerator() {
        return generator;
    }

    public String getVersion() {
        return version;
    }

    public String getMinVersion() {
        return minVersion;
    }
}