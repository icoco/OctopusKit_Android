package com.ixkit.octopus.util;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ixkit.octopuskit.BuildConfig;
import com.orhanobut.logger.Logger;



public class Tracer {
    public static boolean isDebug(){
        return  BuildConfig.DEBUG;

    }
    private static  Gson mGson;
    private static Gson getGsonMaster(){
        if (!isDebug()){
            return null;
        }
        if (null != mGson){
            return mGson;
        }
        mGson = new GsonBuilder().disableHtmlEscaping().create();
        return mGson;
    }

    private static Object object2String(Object obj){
        if (obj instanceof String){
            return (String)obj;
        }
        if (null == obj){
            return "NULL!";
        }
        Gson master = getGsonMaster();
        if (null != master){
            String clsName = obj.getClass().getName();
           return clsName + ":\n" +  master.toJson(obj) ;
        }
        return obj;
    }

    private static Object[]transform2StringObjects(@Nullable Object... args){

        Object[] result = new Object[args.length];
        int i = 0;
        for(Object item: args) {
            result[i] = object2String(item);
            i = i + 1;
        }
        return result;
    }


    public static void d(@Nullable Object... args) {
        if (!isDebug()){
            return;
        }
        Object[] result = transform2StringObjects(args);
        Logger.d(result);
    }
    public static void d(@NonNull String message, @Nullable Object... args) {
        if (!isDebug()){
            return;
        }
        try {
            Object[] result = transform2StringObjects(args);
            Logger.d(message, result);
        }catch (Exception ex){
            Logger.e(ex,"Tracer error ??");
        }
    }

    public static void e(@Nullable Object... args) {
        if (!isDebug()){
            return;
        }
        Object[] result = transform2StringObjects(args);
        Logger.e("",result);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        if (!isDebug()){
            return;
        }
        try {
            Object[] result = transform2StringObjects(args);
            Logger.e(throwable,message, result);
        }catch (Exception ex){
            Logger.e(ex,"Tracer error ??");
        }
    }
}
