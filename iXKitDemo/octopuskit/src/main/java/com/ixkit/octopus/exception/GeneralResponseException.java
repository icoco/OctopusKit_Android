package com.ixkit.octopus.exception;

import com.ixkit.octopus.response.JsonBucket;
import com.ixkit.octopus.response.JosnData;

public class GeneralResponseException extends  WebException{
    public JsonBucket getBean() {
        return bean;
    }

    public void setBean(JsonBucket bean) {
        this.bean = bean;
    }

    private JsonBucket bean;

    public GeneralResponseException(JsonBucket bean){
        super();
        this.bean = bean;
    }

    public String getMessage(){
        String str = super.getMessage();
        if (null != bean){
            JosnData data = bean.getJsonData();
            if (null != data){

                str =   data.pickString("");
            }
        }
        return str;
    }
}
