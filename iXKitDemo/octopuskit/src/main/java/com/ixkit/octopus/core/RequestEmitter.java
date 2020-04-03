package com.ixkit.octopus.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ixkit.octopus.event.WebEvent;
import com.ixkit.octopus.util.RetrofitHelper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by icoco on 15/01/2019.
 */

public class RequestEmitter {


    private static RequestBody toRequestBody(Object requestParameter) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String jsonStr = gson.toJson(requestParameter);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonStr);

        return body;
    }

    public static Call emit(Retrofit master, String route, WebMethod method, BodyType bodyType, HashMap requestParameter, Class classOfResponse, final WebEvent event) {
        return emit(master, route, method, null, bodyType, requestParameter, classOfResponse, event);

    }


    private static Call upload(Retrofit master, String route, Map headers, BodyType bodyType, HashMap requestParameter, Class classOfResponse, final WebEvent event) {
        CallApi api = WebService.create(master, CallApi.class);
        Call call = null;


        HashMap params = requestParameter;

        Map<String, RequestBody> bodyMap = RetrofitHelper.Request.generateRequestBodyMap(params);


        call = api.upload(route, headers, bodyMap);
        WebService.invoke(call, event, classOfResponse);
        return call;
    }

    public static Call emit(Retrofit master, String route, WebMethod method, Map headers, BodyType bodyType, HashMap requestParameter, Class classOfResponse, final WebEvent event) {
        if (null == headers) {
            headers = new HashMap();
        }
        if (null == requestParameter) {
            requestParameter = new HashMap();
        }
        CallApi api = WebService.create(master, CallApi.class);
        Call call = null;
        do {
            if (method == WebMethod.Upload) {

                return upload(master, route, headers, bodyType, requestParameter, classOfResponse, event);
            }
            if (method == WebMethod.Post) {
                if (bodyType == BodyType.form) {
                    HashMap map = requestParameter;

                    call = api.postForm(route, map, headers);
                    break;
                }
                if (bodyType == BodyType.json) {
                    RequestBody parameterBody = toRequestBody(requestParameter);
                    call = api.post(route, parameterBody);
                    break;
                }
                break;
            }
            if (method == WebMethod.Get) {
                HashMap map = requestParameter;
                call = api.get(route, map);
                break;
            }
            if (method == WebMethod.Delete) {
                HashMap map = requestParameter;
                call = api.delete(route, map);
                break;
            }
        } while (false);

        WebService.invoke(call, event, classOfResponse);
        return call;
    }
}
