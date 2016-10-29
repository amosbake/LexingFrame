package com.lexing.common.utils.jsbridge;

/**
 * Created by Asura on 2016/4/22.
 * Java调js的回调
 */
public interface CallBackFunction<T> {
    void onCall(ResponsePack<T> response);
}
