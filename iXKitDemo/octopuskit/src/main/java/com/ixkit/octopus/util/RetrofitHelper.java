package com.ixkit.octopus.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by icoco on 16/01/2019.
 */

public class RetrofitHelper {
    public static  class Request{

        public static RequestBody toRequestBody(String value) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
            return requestBody;
        }

        public static Map toRequestBodys(Map keyValues) {
            if (null == keyValues){return  null;}
            HashMap result = new HashMap();
            Iterator it = keyValues.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if (pair.getValue() instanceof String) {
                    result.put(pair.getKey(), toRequestBody((String)pair.getValue()));
                }
            }
            return result;
        }

        public static RequestBody generateRequestBody(Map keyValues){
            if (null == keyValues){return  null;}

            MultipartBody.Builder builder = new MultipartBody.Builder();


            Iterator it = keyValues.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                String key = (String)pair.getKey();
                Object value = pair.getValue();


                if (value instanceof File) {
                    File file = (File)value;
                    RequestBody fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), fileRQ);
                    builder.addPart(part);
                }else{

                    //if ( value instanceof String)
                    {
                        builder.addFormDataPart(key,(String)value);
                    }
                }

            }

            return builder.build();


        }
        public static Map<String, RequestBody> generateRequestBodyMap(Map keyValues){
            HashMap result = new HashMap();
            if (null == keyValues){return  result;}


            Iterator it = keyValues.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                String key = (String)pair.getKey();
                Object value = pair.getValue();


                if (value instanceof File) {
                    File file = (File)value;
                    RequestBody fileRQ = RequestBody.create(MediaType.parse("*/*"), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), fileRQ);


                    RequestBody fileTwo = RequestBody.create(MediaType.parse("*/*"),  file
                            + file.separator + file.getName() );

                    result.put("file\"; filename=\""+ file.getName(), fileRQ);
                    result.put("file\"; filename=\""+ "2.png", fileTwo);

                }else{

                    result.put(pair.getKey(), toRequestBody((String)pair.getValue()));


                }

            }

            return result;


        }
    }
}
