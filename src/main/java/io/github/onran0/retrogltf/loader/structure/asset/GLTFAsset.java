package io.github.onran0.retrogltf.loader.structure.asset;

import org.json.JSONObject;

import java.util.Optional;

public class GLTFAsset {
    private final String copyright;
    private final String generator;
    private final String version;
    private final String minVersion;

    private final int minorVersion;
    private final int majorVersion;
    private final Integer minorMinVersion;
    private final Integer majorMinVersion;

    public GLTFAsset(JSONObject json) {
        this.copyright = json.optString("copyright");
        this.generator = json.optString("generator");
        this.version = json.getString("version");
        this.minVersion = json.optString("minVersion", null);

        String[] splitVersion = version.split("\\.");

        this.minorVersion = Integer.parseInt(splitVersion[0]);
        this.majorVersion = Integer.parseInt(splitVersion[1]);

        if(this.minVersion != null) {
            String[] splitMinVersion = minVersion.split("\\.");

            minorMinVersion = Integer.parseInt(splitMinVersion[0]);
            majorMinVersion = Integer.parseInt(splitMinVersion[1]);
        } else {
            this.minorMinVersion = null;
            this.majorMinVersion = null;
        }
    }

    public Optional<String> getCopyright() {
        return Optional.ofNullable(copyright);
    }

    public Optional<String> getGenerator() {
        return Optional.ofNullable(generator);
    }

    public String getVersion() {
        return version;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public Optional<String> getMinVersion() {
        return Optional.of(minVersion);
    }

    public int getMinorMinVersion() {
        return minorMinVersion;
    }

    public int getMajorMinVersion() {
        return majorMinVersion;
    }
}