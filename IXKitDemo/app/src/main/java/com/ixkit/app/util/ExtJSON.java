package com.ixkit.app.util;


import org.json.JSONObject;

public class ExtJSON {
    public JSONObject json;
    public String name;

    public ExtJSON(JSONObject json,String name){
        this.json = json;
        this.name = name;
    }
    public String toString(){
        String buf = "json=" + json + "\n" + "name="+ name;
        return buf;
    }
}
