package com.ixkit.octopus.response;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

/**
 * Created by icoco on 09/06/2018.
 */

public class JsonBucket {

    private JosnData jsonData;

    public JosnData getJsonData() {
        return jsonData;
    }

    public void setJsonData(JosnData value) {
        jsonData = value;
    }

    public void attach(String jsonStr) {
        JosnData data = JsonBucket.parseJson(jsonStr);
        jsonData = data;
    }


    private static JosnData parseJson(String jsonStr) {
        GsonPath path = new GsonPath(jsonStr);
        return path;
    }

    public boolean isValidate() {
        return false;
    }

    public String toString() {
        if (null == this.getJsonData()) {
           // return super.toString();
            return  new Gson().toJson(this).toString();
        }
        return this.getJsonData().pickString("");
    }


    public static <T> T loads(String jsonStr, Class<T> cls) {
        T result = null;

        try {
            result = new Gson().fromJson(jsonStr, cls);
            Logger.d(" map result:", result);
        } catch (Exception e) {
            Logger.e("Error %s, jsonStr:%s", e,jsonStr);
        }

        return result;

    }
}