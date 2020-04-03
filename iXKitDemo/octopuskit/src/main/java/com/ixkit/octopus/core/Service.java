package com.ixkit.octopus.core;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by icoco on 06/06/2018.
 */

public abstract class Service<T> {

    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameters(Map<?, ?> parameters) {
        this.mParameters = parameters;
    }


    private Class clzz;

    public Class getClzz() {
        return clzz;
    }

    public void setClzz(Class clzz) {
        this.clzz = clzz;
    }

    private Map<?, ?> mParameters;

    public Map getParameters() {
        return mParameters;
    }

    private Event<?> mEvent;

    public Event<?> getEvent() {
        return mEvent;
    }

    public void setEvent(Event<?> event) {
        this.mEvent = event;
    }

    public void execute(Callback<T> callback) {
        Object[] args = this.getParameters().values().toArray();
        this.execute(this.clzz, this.getMethodName(), args, callback);
    }

    public void execute(Object[] args, Callback<T> callback) {
        this.execute(this.clzz, this.getMethodName(), args, callback);
    }

    public void execute(final Class cls, String method, Object[] args, Callback<T> callback) {

    }

    public void execute(String method, Object[] args, Callback<T> callback) {
        this.execute(this.clzz, method, args, callback);
    }

    public void execute(Call<T> call, Callback<T> callback) {
        call.enqueue(callback);
    }
}
