package com.ixkit.octopus.util;

import java.util.HashMap;

public class HashMapHelper {
    public static <T> HashMap<T, T> toMap(T ... arguments ){
        HashMap<T, T> result = new HashMap<T, T>();
        if (null == arguments){
            return result;
        }
        int count = arguments.length;
        for (int i = 0; i < count; i++){
            T key = arguments[i];
            T  value = arguments[i+1];
            i = i + 1;
            //@step
            if (null != value) {
                result.put(key, value);
            }
        }
        return result;
    }
}
