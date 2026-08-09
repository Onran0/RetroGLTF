package io.github.onran0.retrogltf;

import io.github.onran0.retrogltf.io.IFileProvider;
import io.github.onran0.retrogltf.io.LegacyFileProvider;
import io.github.onran0.retrogltf.io.NIOFileProvider;
import io.github.onran0.retrogltf.structure.access.Buffer;
import io.github.onran0.retrogltf.structure.access.BufferView;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Base64;

public class GLTFLoader {

    private final JSONObject root;
    private final IFileProvider fileProvider;
    private final ByteBuffer glbBuffer;

    public GLTFLoader(JSONObject root) {
        this.root = root;
        this.fileProvider = null;
        this.glbBuffer = null;
    }

    public GLTFLoader(JSONObject root, ByteBuffer glbBuffer) {
        this.root = root;
        this.fileProvider = null;
        this.glbBuffer = glbBuffer;
    }

    public GLTFLoader(JSONObject root, IFileProvider fileProvider) {
        this.root = root;
        this.fileProvider = fileProvider;
        this.glbBuffer = null;
    }

    public GLTFLoader(JSONObject root, File dir) {
        this(root, new LegacyFileProvider(dir));
    }

    public GLTFLoader(JSONObject root, Path dir) {
        this(root, new NIOFileProvider(dir));
    }

    private ByteBuffer getBuffer(String srcUri, Integer byteLength) throws IOException {
        String uri = URLDecoder.decode(srcUri, "UTF-8");

        if(uri.startsWith("data:")) {
            return ByteBuffer.wrap(
                    Base64.getDecoder().decode(uri.substring( uri.indexOf(";base64,") + 9))
            );
        } else {
            int colon = uri.indexOf("/");

            String firstSegment = colon != -1 ? uri.substring(0, colon - 1) : uri;

            if(!firstSegment.contains(":")) {
                if(this.fileProvider == null)
                    throw new IllegalStateException("File provider is undefined");

                return this.fileProvider.getFileData(uri, byteLength);
            } else {
                throw new IllegalArgumentException("Unsupported buffer URI: " + srcUri);
            }
        }
    }

    public void parse() throws IOException {
        JSONArray buffersJson = root.getJSONArray("buffers");

        int buffersCount = buffersJson.length();

        ByteBuffer[] buffers = new ByteBuffer[buffersCount];

        for(int i = 0;i < buffersCount;i++) {
            Buffer bufferObj = new Buffer(buffersJson.getJSONObject(i));

            String uri = bufferObj.getURI();
            int byteLength = bufferObj.getByteLength();

            if(uri == null) {
                if(this.glbBuffer == null)
                    throw new IllegalStateException("GLB buffer is undefined");

                this.glbBuffer.limit(byteLength - 1);
                buffers[i] = glbBuffer;
            } else {
                buffers[i] = getBuffer(uri, byteLength);
            }
        }

        JSONArray bufferViewsJson = root.getJSONArray("bufferViews");

        int viewsCount = bufferViewsJson.length();

        BufferView[] views = new BufferView[viewsCount];

        for(int i = 0;i < viewsCount;i++) {
            views[i] = new BufferView(bufferViewsJson.getJSONObject(i));
        }


    }
}