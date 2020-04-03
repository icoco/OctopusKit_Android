package com.ixkit.octopus.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.ixkit.octopus.util.StringHelper;
import com.orhanobut.logger.Logger;


/**
 * Created by icoco on 09/06/2018.
 */

public class GsonPath implements JosnData {

    private JsonElement obj;

    public GsonPath(String lastResult) {
        this.obj = new JsonParser().parse(lastResult);
    }

    @Override
    public Object pick(String path) {
        JsonElement result = getFromObject(path);
        return result;
    }
    @Override
    public String pickString(String path){
        if (StringHelper.isEmpty(path)){
            return this.obj.toString();
        }
        return getString(path);
    }

    public String getString(String path) {
        JsonElement elt = getFromObject(path);
        if (elt instanceof JsonNull){
            return "";
        }
        String asString = elt.getAsString();
        return asString;
    }
    private JsonElement getFromObject(String path) {
        try{
            return this._getFromObject(path);
        }catch (Exception ex){
            Logger.e("Failed get:%s", path, ex);
        }
        return JsonNull.INSTANCE;

    }

    private static void  _debug_print(String buf){
        if (1>10){
            System.out.println(buf);
        }

    }
    private JsonElement _getFromObject(String path) {
        _debug_print("path:"+path);
        String fullJson = this.obj.toString();
        _debug_print("data:"+fullJson);

        JsonElement jObj = this.obj;
        String[] split = path.split("\\.");
        JsonElement el = null;

        for (String e : split) {
            _debug_print("e:"+e);
            char lastChar = e.charAt(e.length()-1);
            _debug_print("last node char:"+lastChar);

            if(']'==lastChar){
                while (']'==lastChar) {
                    _debug_print("e+:"+e);
                    _debug_print("e.length()-1: "+(e.length()-1));
                    _debug_print("e.lastIndexOf('[')+1: "+(e.lastIndexOf('[')+1));
                    String index = e.substring(e.lastIndexOf('[')+1, e.length()-1);
                    Integer iindex = Integer.valueOf(index);
                    _debug_print("iindex:"+iindex);
                    e = e.substring(0,e.lastIndexOf('['));
                    if("".equals(e)){
                        if(el==null){
                            el = jObj.getAsJsonArray().get(iindex);
                            lastChar = (char)0;
                        } else {
                            el = el.getAsJsonArray().get(iindex);
                            lastChar = (char)0;
                        }
                    } else {
                        lastChar = e.charAt(e.length()-1); // new last char
                        _debug_print("new lastChar is:"+lastChar);


                        // if next is object
                        if(lastChar!=']'){
                            if(el==null){
                                el = jObj.getAsJsonObject().get(e).getAsJsonArray().get(iindex);
                            } else {
                                _debug_print("el:"+el+", path:"+e);
                                if(el.isJsonObject()){
                                    el = el.getAsJsonObject().get(e).getAsJsonArray().get(iindex);
                                } else if(el.isJsonArray()){
                                    el = el.getAsJsonArray().get(iindex);
                                }
                            }
                        } else { // next is array
                            if(el==null){
                                if(jObj.isJsonObject()){
                                    String locale = e.substring(0,e.indexOf('['));
                                    _debug_print("el:"+el+", path:"+locale);
                                    el = jObj.getAsJsonObject().get(locale).getAsJsonArray().get(iindex);
                                } else if(jObj.isJsonArray()){
                                    el = jObj.getAsJsonArray().get(iindex);
                                }
                            } else {
                                if(e.indexOf('[')>-1){
                                    String locale = e.substring(0,e.indexOf('['));
                                    _debug_print("el:"+el+", path:"+locale);
                                    el = el.getAsJsonObject().get(locale).getAsJsonArray().get(iindex);
                                } else {
                                    el = el.getAsJsonArray().get(iindex);
                                    _debug_print("el:"+el);
                                };

                            }
                        }








                        _debug_print("index:"+index);
                        _debug_print("new e:"+e);
                        // array here
                        // TODO
                        _debug_print("----");


                    }
                }
            } else{
                // plain obj
                if(el==null){
                    el = jObj.getAsJsonObject().get(e);
                } else {
                    el = el.getAsJsonObject().get(e);
                }
                _debug_print("node val:"+el.toString());

            }

        }
        return el;
    }

    public static void main(String[] args) {
        String asString ;
        String json;

        json = "{\"a\":{\"b\":{\"c\":\"val\"}}}";
        asString = new GsonPath(json).getString("a.b.c");
        System.err.println(asString);

        json = "{\"a\":{\"b\":[{\"c\":\"val\"}]}}";
        asString = new GsonPath(json).getString("a.b[0].c");
        System.err.println(asString);

        json = "{\"a\":[{\"b\":{\"c\":\"val\"}}]}";
        asString = new GsonPath(json).getString("a[0].b.c");
        System.err.println(asString);

        json = "[{\"b\":{\"c\":\"val\"}},{}]";
        asString = new GsonPath(json).getString("[0].b.c");
        System.err.println(asString);

        json = "{a:{b:{c:[[val]]}}}";
        asString = new GsonPath(json).getString("a.b.c[0][0]");
        System.err.println(asString);


        json = "{\"a\":{\"b\":[[{\"c\":\"val\"}]]}}";
        asString = new GsonPath(json).getString("a.b[0][0].c");
        System.err.println(asString);


        json = "{\"a\":[[{\"b\":{\"c\":\"val\"}}]]}";
        asString = new GsonPath(json).getString("a[0][0].b.c");
        System.err.println(asString);

        json = "[[{\"b\":{\"c\":\"val\"}},{}]]";
        asString = new GsonPath(json).getString("[0][0].b.c");
        System.err.println(asString);
    }


}
