package com.lexing.common.utils.jsbridge;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Asura on 2016/4/26.
 */
public class ResponsePack<T> {
    @SerializedName("data")
    private T data;
    @SerializedName("errorCode")
    private int errorCode;
    @SerializedName("errorMsg")
    private String errorMsg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ResponsePack{" +
                "data=" + data +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
