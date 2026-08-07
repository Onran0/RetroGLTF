package io.github.onran0.retrogltf.util;

import org.joml.*;
import org.json.*;

import java.util.ArrayList;
import java.util.function.Function;

public class JSONUtil {

    public static <T> T parseNullableObject(JSONObject obj, String key, Function<JSONObject, T> parser) {
        return parseObjectOpt(obj, key, parser, null);
    }

    public static <T> T parseObjectOpt(JSONObject obj, String key, Function<JSONObject, T> parser, T def) {
        if(obj.has(key)) {
            return parser.apply(obj.getJSONObject(key));
        } else {
            return def;
        }
    }

    public static Integer getNullableInt(JSONObject obj, String key) {
        return obj.has(key) ? obj.getInt(key) : null;
    }

    public static Float getNullableFloat(JSONObject obj, String key) {
        return obj.has(key) ? obj.getFloat(key) : null;
    }

    public static int[] toIntArray(JSONArray arr) {
        if(arr == null)
            return null;

        int count = arr.length();

        int[] res = new int[count];

        for(int i = 0; i < count; i++) {
            res[i] = arr.getInt(i);
        }

        return res;
    }

    public static float[] toFloatArray(JSONArray arr) {
        if(arr == null)
            return null;

        int count = arr.length();

        float[] res = new float[count];

        for(int i = 0; i < count; i++) {
            res[i] = arr.getFloat(i);
        }

        return res;
    }

    public static <T> T[] toObjectArray(JSONArray arr, Function<Integer, T[]> arrCreator, Function<JSONObject, T> parser) {
        if(arr == null)
            return null;

        T[] res = arrCreator.apply(arr.length());

        int count = arr.length();

        for(int i = 0; i < count; i++) {
            res[i] = parser.apply(arr.getJSONObject(i));
        }

        return res;
    }

    public static Vector3f toVector3(JSONArray arr) {
        return toVector3(arr, null);
    }

    public static Vector4f toVector4(JSONArray arr) {
        return toVector4(arr, null);
    }

    public static Quaternionf toQuaternion(JSONArray arr) {
        return toQuaternion(arr, null);
    }

    public static Vector3f toVector3(JSONArray arr, Vector3f def) {
        if(arr == null)
            return def;

        return new Vector3f(
                arr.getFloat(0),
                arr.getFloat(1),
                arr.getFloat(2)
        );
    }

    public static Vector4f toVector4(JSONArray arr, Vector4f def) {
        if(arr == null)
            return def;

        return new Vector4f(
                arr.getFloat(0),
                arr.getFloat(1),
                arr.getFloat(2),
                arr.getFloat(3)
        );
    }

    public static Quaternionf toQuaternion(JSONArray arr, Quaternionf def) {
        if(arr == null)
            return def;

        return new Quaternionf(
                arr.getFloat(0),
                arr.getFloat(1),
                arr.getFloat(2),
                arr.getFloat(3)
        );
    }

    public static Matrix4f toMatrix4(JSONArray arr) {
        if(arr == null)
            return null;

        return new Matrix4f(
                arr.getFloat(0), arr.getFloat(1), arr.getFloat(2),arr.getFloat(3),
                arr.getFloat(4), arr.getFloat(5), arr.getFloat(6),arr.getFloat(7),
                arr.getFloat(8), arr.getFloat(9), arr.getFloat(10),arr.getFloat(11),
                arr.getFloat(12), arr.getFloat(13), arr.getFloat(14),arr.getFloat(15)
        );
    }
}