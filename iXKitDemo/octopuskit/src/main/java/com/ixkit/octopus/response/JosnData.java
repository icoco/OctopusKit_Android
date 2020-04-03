package com.ixkit.octopus.response;

/**
 * Created by icoco on 08/06/2018.
 */

public interface JosnData {
    public Object pick(String path);


    public String pickString(String path);
}
