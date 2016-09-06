package com.ixkit.app.util;

import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Printer;

import org.json.JSONObject;

public final class Tracer {
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;




    //no instance
    private Tracer() {
    }

    static {
       Logger.init("ixkit");
    }

    public static void init (String tag){
        Logger.init(tag);
    }

    public static Printer t(String tag) {
        return Logger.t(tag);

    }

    public static void t(int methodCount) {
         Logger.t(null, methodCount);
    }

    public static void t(String tag, int methodCount) {
          Logger.t(tag, methodCount);
    }

    public static void log(int priority, String tag, String message, Throwable throwable) {
        Logger.log(priority, tag, message, throwable);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void e(String message, Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void v(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void w(String message, Object... args) {
        Logger.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        Logger.json(json);
    }

    public static void json(JSONObject json) {
        if (null == json){
            Logger.json(null);
            return ;
        }

        Logger.json(json.toString());
    }
    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        Logger.xml(xml);
    }
}