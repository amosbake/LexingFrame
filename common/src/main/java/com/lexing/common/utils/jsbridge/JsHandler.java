package com.lexing.common.utils.jsbridge;

/**
 * Created by Asura on 2016/4/23.
 */
public interface JsHandler<T,R> {
    ResponsePack<R> handle(T params);
}
