package com.lexing.common.utils.jsbridge;

/**
 * Created by Asura on 2016/4/22.
 * 桥，实现java和js互相调用
 */
public interface JsBridge {
    /**
     * 给js提供调用处理
     *
     * @param actionName 功能名,比如getUserName
     * @param handler
     */
    void registerJsHandler(String actionName, JsHandler<?, ?> handler);

    void unRegisterJsHandler(String actionName);

    void callJsFunction(String actionName, Object params, CallBackFunction<?> callBackFunction);
}
