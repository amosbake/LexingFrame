package com.lexing.lexingframe.ganhuo;

import com.google.gson.annotations.SerializedName;

/**
 * Author: mopel
 * Date : 2016/10/27
 */
public class GanhuoData<T> {
    @SerializedName("results")
     T data;
     boolean error;

    public T getData() {
        return data;
    }
}
