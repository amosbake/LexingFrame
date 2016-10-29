package com.lexing.common.utils.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Asura on 2016/2/16.
 */
public class NumberAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if(rawType == Integer.class || rawType == int.class) {
            return (TypeAdapter<T>) new IntTypeAdapter();
        }
        if(rawType == Long.class || rawType == long.class){
            return (TypeAdapter<T>) new LongTypeAdapter();
        }
        return null;
    }
}
