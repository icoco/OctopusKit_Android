package com.ixkit.app.model;

import android.content.Context;

import com.ixkit.app.AppManager;
import com.ixkit.app.util.AssetHelper;
import com.ixkit.app.util.Tracer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiModel {

    private static ApiModel instance;

    private ApiModel(){}

    public static ApiModel getInstance(){
        if(instance==null){
            instance=new ApiModel();
        }
        return instance;
    }

    private JSONObject data;

    public JSONObject getApiAsJson(){
        if (  null != data) {
            return data;
        }

        Context context = AppManager.getInstance().getContext();
        String buf = AssetHelper.loadFile(context,"api.json");

        JSONObject obj = null;
        try {
            obj = new JSONObject(buf);
        } catch (JSONException e) {
            Tracer.e(e,"getApiAsJson");
        }
        data = obj;
        return data;
    }


    public JSONArray getModules(){
        JSONArray modules = null;

        try {
            modules = this.getApiAsJson().getJSONArray("modules");
        }
        catch (Exception e) {
            Tracer.e(e,"getModules");
        }
        return modules;
    }


}
