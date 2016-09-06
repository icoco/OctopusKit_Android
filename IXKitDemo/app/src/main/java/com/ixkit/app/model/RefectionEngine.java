/**
 * RefectionEngine
 *
 *
 * For more information, visit the project page:
 * https://github.com/icoco/ixkit
 *
 * @author Robin Cheung <iRobinCheung@hotmail.com>
 * @version 1.0.1
 */
package com.ixkit.app.model;


import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.Response;

import com.ixkit.app.util.JSONHelper;
import com.ixkit.app.util.ReflectionHelper;
import com.ixkit.app.util.Tracer;


public class RefectionEngine {
    public static final String TAG = "RefectionEngine";

    private static Listener<String> onSuccessCallback(){
        //@step
        Response.Listener<String> onSuccess = new Listener<String>() {
            @Override
            public void onResponse(String s) {
                Tracer.t(TAG).d("onSuccess->",s);
            }
        };
        return onSuccess;
    }
    private static ErrorListener onErrorCallBack(){
        Response.ErrorListener onError = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Tracer.t(TAG).d("onError->",volleyError);

            }
        };
        return onError;
    }

    private static Class stringToClass(String type){
        if (null == type)return null;
        if ("String".equalsIgnoreCase(type)){
            return String.class;
        }
        if ("Integer".equalsIgnoreCase(type)){
            return Integer.class;
        }
        if ("Number".equalsIgnoreCase(type)){
            return Number.class;
        }
        //@step default
        return String.class;
    }

    private static Class[] toParameterTypes(JSONArray parameters) throws Exception{

        if (null == parameters) return null;
        int size = parameters.length();
        Class[] parameterTypes = new Class[size];
        for (int i = 0; i < size ; i++) {
            JSONObject item = (JSONObject)parameters.get(i);
            String type = JSONHelper.getString(item,"type");
            Class cls = RefectionEngine.stringToClass(type);
            if (null == cls) continue;
            parameterTypes[i]= cls;
        }
        return parameterTypes;
    }

    private static Object[] toArguments(JSONArray parameters) throws Exception{

        if (null == parameters) return null;
        int size = parameters.length();
        Object[] arguments = new Object[size];
        for (int i = 0; i < size ; i++) {
            JSONObject item = (JSONObject)parameters.get(i);

            String type = JSONHelper.getString(item,"type");
            Class cls = RefectionEngine.stringToClass(type);
            if (null == cls) continue;
            Object value = null;
            if (String.class == cls){
                  value = JSONHelper.getString(item,"value");
            }
            if (Integer.class == cls){
                value = item.getInt("value");
            }
            if (Number.class == cls){
                value = item.getDouble("value");
            }
            arguments[i]= value;
        }
        return arguments;
    }
    /*
     { "title":"getProduct",
          "url" :"http://google.com",
          "java" :{
            "class":"com.ixkit.iapp.service.CategoryService",
            "method":"getProduct"
          },

          "parameters":[
            {
              "name":"proudctId",
              "value":"0",
              "type" : "String"
            },
            {
              "name":"key",
              "value":9,
              "type" : "Integer"
            },
            {
              "name":"rate",
              "value":9090.22,
              "type" : "Number"
            }

          ]


        },
     */
    public static Object invoke(JSONObject item,Listener<String> onSuccessCallback,
                              ErrorListener onErrorCallBack){
        Object result = null;
        try {
            String className = JSONHelper.getString(item,"lang.class");
            String methodName = JSONHelper.getString(item,"lang.method");

            JSONArray parameters = item.getJSONArray("parameters");
            Class[] parameterTypes = RefectionEngine.toParameterTypes(parameters);

            Object[] arguments = RefectionEngine.toArguments(parameters);

            Object service =  ReflectionHelper.invoke(className,methodName,parameterTypes,null,arguments);

            result = service;

            //@step
            Class[] paramClass = new Class[2];
            paramClass[0] = Response.Listener.class;
            paramClass[1] = Response.ErrorListener.class;

            //@step
            Response.Listener<String> success = null == onSuccessCallback ?
                    RefectionEngine.onSuccessCallback() : onSuccessCallback;

            Response.ErrorListener error = null == onErrorCallBack ? RefectionEngine.onErrorCallBack() :onErrorCallBack;

            //@step
            ReflectionHelper.invoke(service,"execute",paramClass,success,error );


        }catch (Exception e){
            Tracer.e(e, "invoke");
        }
        return result;
    }

    public static void invoke(JSONObject item){
        RefectionEngine.invoke(item,null,null);
    }
}
