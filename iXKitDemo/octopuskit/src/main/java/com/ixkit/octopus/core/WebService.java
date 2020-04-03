package com.ixkit.octopus.core;

import com.google.gson.Gson;
import com.ixkit.octopus.exception.GeneralResponseException;
import com.ixkit.octopus.response.JsonBucket;
import com.orhanobut.logger.Logger;

import com.ixkit.octopus.util.Tracer;

import com.ixkit.octopus.event.WebEvent;
import com.ixkit.octopus.exception.WebException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by icoco on 07/06/2018.
 */

public class WebService extends Service {

    public static <T> T create(Retrofit retrofit, final Class<T> cls) {
        return retrofit.create(cls);
    }

    private static <T> T response2Object(Response response, Class<T> cls) {
        Logger.d("response:[%s],to class name:[%s]", response, cls.getName());

        String buf = null;//  g.toJson(response.body());
        if (200 == response.code()) {
            try {
                ResponseBody b = (ResponseBody) response.body();
                buf = b.string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ResponseBody b = (ResponseBody) response.errorBody();
                buf = b.string();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Logger.d("response string: %s", buf);


        return jsonStr2Object(buf, cls);
    }

    private static <T> Class<T> getexpectedClass(WebEvent event) {
        TypeVariable<?>[] cls = event.getClass().getTypeParameters();
        if (null == cls || cls.length < 1) {
            return null;
        }
        return (Class<T>) cls[0].getClass();
    }

    public static <T> void invoke(Call call, final WebEvent event, final Type returnType) {
        Class clazz = null == returnType ? null : returnType.getClass();
        invoke(call, event, clazz);
    }

    public static <T> Class<T> invoke(Call call, final WebEvent event, final Class<T> expectedClass) {
        Logger.d("WebService->invoke:%s,expectedClass:%s", call.request(), expectedClass);
        Callback<ResponseBody> hook = new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null == event) {
                    Logger.e( "onResponse-> miss callback:%s",response);
                    return;
                }
                //@step
                int code = response.code();
                if (200 != code) {
                    fireException(call, response);
                    return;
                }

                Object expectedObject = WebService.response2Object(response, expectedClass);
                if (null != expectedObject && expectedClass.isInstance(expectedObject)) {
                    event.onResponse(expectedObject);
                    return;
                }
                //exception case
                if (null != expectedObject && expectedObject instanceof JsonBucket) {
                    WebException exception = new GeneralResponseException((JsonBucket) expectedObject);
                    event.onFailure(exception);
                    return;
                }
                if (null == expectedObject) {
                    fireException(call, response);
                    return ;
                }

                Tracer.e("Miss Handle ?? %s", response);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.e(t, "onFailure call:%s", call);
                if (null == event) {
                    return;
                }

                WebException exception = new WebException(t);
                event.onFailure(exception);
            }

            private void fireException(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (null != response) {
                    int code = response.code();
                    Map<String, List<String>> responseHeaders = response.headers().toMultimap();
                    String buf = "";
                    try {
                        if (null != response.body()) {
                            buf = response.body().string();
                        }else{
                            buf = response.errorBody().string();
                        }
                    } catch (Exception ex) {
                        Logger.e(ex, "Failed get response body string! %s", response);
                    }

                    WebException exception = new WebException(code, responseHeaders, buf);
                    event.onFailure(exception);
                    return;
                }

                Throwable t = new Throwable("no response!");
                WebException exception = new WebException(t);
                event.onFailure(exception);

            }
        };

        call.enqueue(hook);
        return null;
    }


    private static <T> T jsonStr2Object(String jsonStr, Class<T> cls) {
        T result = null;
        if (null != cls) {
            try {

                result = new Gson().fromJson(jsonStr, cls);
                //Logger.d("jsonStr2Object class:%s, result:%s", cls, result);
                if (result instanceof JsonBucket ||
                        JsonBucket.class.isAssignableFrom(result.getClass())) {
                    JsonBucket bean = (JsonBucket) result;
                    bean.attach(jsonStr);
                    //Logger.d("attach json to :%s", result);
                }
                return result;
            } catch (Exception e) {
                Logger.e("Error %s", e);
            }
        }


        if (null == result) {
            try {
                Object o = new Gson().fromJson(jsonStr, JsonBucket.class);
                result = (T) o;
                Logger.d(" Failed map input then return JsonBucket : %s", result);
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        }
        if (null != result) {
            if (result instanceof JsonBucket ||
                    JsonBucket.class.isAssignableFrom(result.getClass())) {
                JsonBucket bean = (JsonBucket) result;
                bean.attach(jsonStr);
                Logger.d("attach json to :%s", result);
            }
        }
        return result;
    }


}
