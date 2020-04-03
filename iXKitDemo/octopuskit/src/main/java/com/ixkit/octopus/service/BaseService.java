package com.ixkit.octopus.service;



import com.ixkit.network.retrofit.RetrofitMaster;

import com.ixkit.octopus.core.BodyType;
import com.ixkit.octopus.core.RequestEmitter;
import com.ixkit.octopus.core.WebMethod;
import com.ixkit.octopus.event.WebEvent;
import com.ixkit.octopus.response.JsonBucket;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;

public  class BaseService {
    
        private String route;
        private WebMethod method = WebMethod.Post;
        private HashMap headers;
        private HashMap requestParameter;
        private Class responseClass; // default is JsonBucket
        private WebEvent event;



        private BodyType bodyType = BodyType.json;

        public BaseService setBodyType(BodyType bodyType) {
            this.bodyType = bodyType;
            return this;
        }

        public BodyType getBodyType() {
            return bodyType;
        }

        public BaseService setRoute(String route){
            this.route = route;
            return this;
        }
        public BaseService setMethod(WebMethod method){
            this.method = method;
            return this;
        }
        public BaseService setRequestParameter(HashMap params){
            this.requestParameter = params;
            return this;
        }

    public HashMap getHeaders() {
        return headers;
    }

    public BaseService setHeaders(HashMap headers) {
        this.headers = headers;
        return this;
    }

    public BaseService setResponseClass(Class clazz){
            this.responseClass = clazz;
            return this;
        }
        public BaseService setListner(final WebEvent event){
            this.event = event;
            return  this;
        }
    
        //@TODO  need handle the multi-  execute call?
        private Call call;
        public void cancel(){
            if (null != call){
                call.cancel();
            }
        }

        public Retrofit getRetrofit() {

                return   RetrofitMaster.getInstance().getRetrofit();

        }
    

    
        public BaseService execute(final WebEvent event){
            //@TODO  need handle the multi-  execute call?
            Class clazz = null == this.responseClass ? JsonBucket.class : this.responseClass;

            this.call = RequestEmitter.emit(getRetrofit(),this.route,this.method, this.bodyType, this.requestParameter,clazz,event);
            return this;
        }
    
    }