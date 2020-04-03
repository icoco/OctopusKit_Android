package com.ixkit.octopus.event;

public interface WebEvent<T> {
    void onResponse(T response);

    void onFailure(Throwable t);
}
