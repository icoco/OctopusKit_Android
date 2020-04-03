package com.ixkit.octopus.core;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by icoco on 08/06/2018.
 */

public interface CallApi {
    //Get
    @GET("{route}")
    Call<ResponseBody> get(@Path(value = "route", encoded = true) String route, @QueryMap Map<String, Object> parameters);

    @GET("{route}")
    Call<ResponseBody> get(@Path(value = "route", encoded = true) String route, @QueryMap Map<String, Object> parameters, @HeaderMap Map<String, String> headers);

    //Post
    @POST("{route}")
    Call<ResponseBody> post(@Path(value = "route", encoded = true) String route, @QueryMap Map<String, Object> parameters);

    @POST("{route}")
    Call<ResponseBody> post(@Path(value = "route", encoded = true) String route, @QueryMap Map<String, Object> parameters, @HeaderMap Map<String, String> headers);

    @POST("{route}")
    Call<ResponseBody> post(@Path(value = "route", encoded = true) String route, @Body RequestBody requestBody);

    @POST("{route}")
    Call<ResponseBody> post(@Path(value = "route", encoded = true) String route, @Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST("{route}")
    Call<ResponseBody> postForm(@Path(value = "route", encoded = true) String route, @FieldMap Map<String, Object> fieldMap, @HeaderMap Map<String, String> headers);


    //Delete
    @DELETE("{route}")
    Call<ResponseBody> delete(@Path(value = "route", encoded = true) String route, @QueryMap Map<String, Object> parameters);

    @DELETE("{route}")
    Call<ResponseBody> delete(@Path(value = "route", encoded = true) String route, @QueryMap Map<String, Object> parameters, @HeaderMap Map<String, String> headers);

    @DELETE("{route}")
    Call<ResponseBody> delete(@Path(value = "route", encoded = true) String route, @Body RequestBody requestBody);

    @DELETE("{route}")
    Call<ResponseBody> delete(@Path(value = "route", encoded = true) String route, @Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    //Upload
    @POST("{route}")
    Call<ResponseBody> upload( @Path(value = "route", encoded = true) String route,
                              @HeaderMap Map<String, String> headers,
                               @Header("X-CSRFToken") String token,
                              @Body RequestBody body);


    @Multipart
    @POST("{route}")
    Call<ResponseBody> upload(@Path(value = "route", encoded = true) String route,

                              @HeaderMap Map<String, String> headers,
                              @PartMap Map<String, RequestBody> bodyMap);
    @Deprecated
    @Multipart
    @POST("{route}")
    Call<ResponseBody> upload(@Path(value = "route", encoded = true) String route,
                              @PartMap Map<String,RequestBody> data,
                              @HeaderMap Map<String, String> headers,
                              @Part("file") RequestBody name,
                              @Part MultipartBody.Part file);

}
