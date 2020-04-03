package com.ixkit.octopus.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

public class Argument<K, V> {
    public static Argument create() {
        return new Argument();
    }

    public static <T> Argument<T, T> create(T... arguments) {
        Argument result = new Argument();
        result.extra = HashMapHelper.toMap(arguments);
        return result;
    }

    public Argument() {

    }

    private HashMap<K, V> extra;

    public HashMap<K, V> getData() {
        return extra;
    }

    public V get(K key) {
        if (null == extra) {

            return null;
        }
        return extra.get(key);
    }

    public <T> T get(K key, Class<T> cls) {
        String buf = getString(key);
        return jsonString2Object(buf, cls);

    }

    public static <T> T jsonString2Object(String jsonStr, Class<T> cls) {
        T result = null;
        if (null == jsonStr || "" == jsonStr || null == cls) {
            return result;
        }
        if (null != cls) {
            try {
                result = new Gson().fromJson(jsonStr, cls);
                Logger.d("jsonString2Object map-class:%s, result:%s", cls, result);
                return result;
            } catch (Exception e) {
                Logger.e("Error %s", e);
            }
        }
        return result;
    }

    public static String object2String(Object obj) {
        return object2String(obj,false);
    }

    public static String object2String(Object obj, boolean escapeHtmlChars) {

        if (null == obj) {
            return "";
        }
        if (obj instanceof String) {
            return (String) obj;
        }

        if (escapeHtmlChars){
            Gson master = new GsonBuilder().create();
            return master.toJson(obj);
        }

        Gson master = new GsonBuilder().disableHtmlEscaping().create();
        //   String clsName = obj.getClass().getName();
        return master.toJson(obj);

    }
    //    public V getOrDefault(K key, V defaultValue){
//        if (null == extra){
//           return null;
//        }
//        V result = extra.get(key);
//        if (null == result){
//            return defaultValue;
//        }
//        return  result;
//
//    }
    public String getString(K key) {

        try {
            String result = object2String(get(key));
            return result;
        } catch (Exception ex) {
            Logger.e(ex, "Failed getString %s", key);
        }
        return "";
    }

    public Boolean getBoolean(K key) {
        return (Boolean) get(key);
    }

    public V get(K key, V defaultValue) {
        if (null == extra) {
            extra = new HashMap<>();
        }
        V r = extra.get(key);
        if (null == r) {
            return defaultValue;
        }
        return r;
    }

    public Argument put(K key, V value) {
        if (null == extra) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
        return this;
    }

    public String toString() {
        String buf = super.toString();
        buf = buf + "\n extra:" + this.extra;
        return buf;
    }
}
