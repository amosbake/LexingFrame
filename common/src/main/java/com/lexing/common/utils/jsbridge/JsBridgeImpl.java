package com.lexing.common.utils.jsbridge;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lexing.common.utils.gson.BooleanAdapter;
import com.lexing.common.utils.gson.StringAdapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Asura on 2016/4/22.
 */
public class JsBridgeImpl implements JsBridge {
    private static final String JS_BRIDGE_PROTOCOL_SCHEMA = "jsbridge://";
    private static final String ACTION_RETURN = "return";
    private WebView mWebView;
    private static final ArrayList<String> mHostWhiteList;
    private Map<String, JsHandler> mJsHandlerMap;//functionName---->handler
    private Map<String, CallBackFunction> mCallBackMap;//callback_id--->callback
    private int mCounter;//简单的计数器，辅助生成callbackId
    private CompositeSubscription mSubscriptions;
    private Gson mGson;

    static {
        //白名单
        mHostWhiteList = new ArrayList<>();
        mHostWhiteList.add("192.168.1.46");
        mHostWhiteList.add("192.168.0.102");
    }

    private final Handler handler;

    public void addToWhiteList(String host) {
        mHostWhiteList.add(host);
    }

    public JsBridgeImpl(WebView webView) {
        this.mWebView = webView;
        handler = new Handler(Looper.getMainLooper());
        mJsHandlerMap = new HashMap<>();
        mCallBackMap = new HashMap<>();
        mSubscriptions = new CompositeSubscription();
        mGson = new GsonBuilder().registerTypeAdapter(String.class, new StringAdapter())
                .registerTypeAdapter(Boolean.class, new BooleanAdapter())
                .registerTypeAdapter(boolean.class, new BooleanAdapter()).serializeNulls().create();
    }


    @Override
    public void registerJsHandler(String actionName, JsHandler<?, ?> handler) {
        if (ACTION_RETURN.equals(actionName)) {
            throw new RuntimeException("you can`t use return for your action name");
        }
        mJsHandlerMap.put(actionName, handler);
    }

    @Override
    public void unRegisterJsHandler(String actionName) {
        mJsHandlerMap.remove(actionName);
    }

    //可以调用多次
    @Override
    public void callJsFunction(final String actionName, final Object jsonObj, final CallBackFunction<?> callBackFunction) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String callBackId = generateCallBackId();
                mCallBackMap.put(callBackId, callBackFunction);
                String params = "{}";
                if (jsonObj != null) {
                    params = mGson.toJson(jsonObj);
                }
                mWebView.loadUrl("javascript:JsBridge.onReceive('" + actionName + "','" + params + "','" + callBackId + "');");
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }

    public void onDestroy() {
        mJsHandlerMap.clear();
        mJsHandlerMap = null;
        mCallBackMap.clear();
        mCallBackMap = null;
        if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
        mSubscriptions = null;
        mHostWhiteList.clear();
        mWebView = null;
        mGson = null;
    }


    /**
     * 生成唯一的id串
     *
     * @return
     */
    private String generateCallBackId() {
        return (mCounter++) + UUID.randomUUID().toString();
    }

    /**
     * 对要跳转的url进行分析
     *
     * @param url
     * @return false不处理，true处理
     */
    public boolean handleUrlAction(String url) {
        int flag = 0;
        try {
            url = URLDecoder.decode(url, "UTF-8");
            if (!url.startsWith(JS_BRIDGE_PROTOCOL_SCHEMA)) {
                return false;
            }
            flag = 1;
            checkWhiteList();
            Uri uri = Uri.parse(url);
            String action = uri.getHost();
            String[] split = uri.getPath().substring(1).split("/");
            final String params = split[0];
            final String callbackId = split[1];
            if (ACTION_RETURN.equals(action)) {
                //js返回数据给java
                CallBackFunction callback = mCallBackMap.get(callbackId);
                Type type = ((ParameterizedType) (callback.getClass().getGenericInterfaces()[0])).getActualTypeArguments()[0];
                if (callback != null) {
                    ResponsePack data = mGson.fromJson(params, new ResponsePackParameterizedType(type));
                    callback.onCall(data);
                    mCallBackMap.remove(callbackId);
                }
                return true;
            } else {
                final JsHandler jsHandler = mJsHandlerMap.get(action);
                if (jsHandler != null) {
                    final Type type = ((ParameterizedType) (jsHandler.getClass().getGenericInterfaces()[0])).getActualTypeArguments()[0];
                    Subscription subscription = Observable.create(new Observable.OnSubscribe<String[]>() {
                        @Override
                        public void call(Subscriber<? super String[]> subscriber) {
                            String[] strings = new String[2];
                            ResponsePack responsePack = jsHandler.handle(mGson.fromJson(params, type));
                            strings[0] = responsePack == null ? "{}" : mGson.toJson(responsePack);
                            strings[1] = callbackId;
                            subscriber.onNext(strings);
                            subscriber.onCompleted();
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String[]>() {
                        @Override
                        public void onCompleted() {
                            if (!isUnsubscribed()) {
                                unsubscribe();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            onCompleted();
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(String[] strings) {
                            if (!isUnsubscribed()) {
                                sendToJs(strings[0], strings[1]);
                            }
                        }
                    });
                    mSubscriptions.add(subscription);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (flag == 1) return true;
        }
        return false;
    }

    private void sendToJs(String result, String callbackId) {
        mWebView.loadUrl("javascript:JsBridge.onReceive('" + ACTION_RETURN + "','" + result + "','" + callbackId + "');");
    }


    /**
     * 白名单确保只对认识的域名下的页面提供接口
     *
     * @return
     */
    private boolean checkWhiteList() {
        String str = mWebView.getUrl();
        URL url = null;
        try {
            url = new URL(str);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (mHostWhiteList.indexOf(url.getHost()) == -1) {
            throw new RuntimeException("you have no permission to call this");
        }
        return true;
    }

    private static class ResponsePackParameterizedType implements ParameterizedType {

        private Type type;

        private ResponsePackParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{type};
        }

        @Override
        public Type getRawType() {
            return ResponsePack.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ResponsePackParameterizedType)) return false;

            ResponsePackParameterizedType that = (ResponsePackParameterizedType) o;

            return !(type != null ? !type.equals(that.type) : that.type != null);

        }
    }
}
