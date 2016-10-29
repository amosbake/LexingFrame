js和native通信
===

### 原理
> 1. js调用native方法：js通过url跳转的方式，让webview拦截到url，然后根据自定义的协议判断js动作。    
> 2. native调用js：js注册好功能,并提供一个onReceive方法给native传递数据。以android为例，使用```webview.loadUrl("javascript:onReceive(...)")```这种形式传递数据。   

协议约定: ```jsbridge://[action]/[params]/[callback_id]```  
* action: 功能名，native指定的接口名，给js调用（注意return作为关键字由内部使用）   
* params: JSON格式的参数或者回传的数据（格式为```{"errorCode":0,"errorMsg":"success","data":{}}```）    
* callback_id: 用来标识回调函数。 js调用java获取数据的时候，callback_id是js用来标识callback的，方便native执行完成之后传递数据；
java调js，用来标识java层的回调函数，给js一个callback_id，js执行操作把数据返回给java时，再带上这个callback_id，然后交由java解析。  


### 流程简介
1. js调native流程
> JsBridge.callNative->js注册callback->协议跳转->native操作->native返回数据->协议跳转->拿到注册的callback->js调用

2. native调js
> JsBridge.registerHandler->native调用js并记录callback_id->js执行返回数据->native通过callback_id找到注册的回调->调用


### API文档

#### js部分
> window对象上会挂载一个JsBridge对象，js调用native如下：

```javascript
JsBridge.callNative('getUserName', {id: 1, type: "类型"}, function (data) {
	//data是JsBridge.Response的实例
	alert(JSON.stringify(data));//{"errorCode":0,"errorMsg":"success","data":{"name":"xxx"}}
});
```
> js注册功能提供给native调用如下：

```javascript
JsBridge.registerHandler('getColorNeedTime', function (req, res) {
	console.log("getColorNeedTime req:" + JSON.stringify(req));
	//可做网络操作后send
	res.send(new JsBridge.Response(0, "ok", {color: "red", like: 100}));
});
```

>js 对某个功能注销

```javascript
JsBridge.unregisterHandler('getColorNeedTime');
```

> js传给native的数据和native传给js的数据都必须是固定的json格式  
```{"errorCode":0,"errorMsg":"success","data":{"name":"xxx"}}```

```javascript
function Response(code, msg, data) {
	this.errorCode = code;
	this.errorMsg = msg;
	this.data = data;
}

Response.prototype = {
	setErrorCode: function (code) {
		this.errorCode = code;
		return this;
	},
	setErrorMsg: function (msg) {
		this.errorMsg = msg;
		return this;
	},
	setData: function (data) {
		this.data = data;
		return this;
	}
}
```

#### native部分(android)

```java
public interface JsBridge {
    /**
     * 给js提供调用处理
     *
     * @param actionName 功能名,比如getUserName
     * @param handler
     */
    void registerJsHandler(String actionName, JsHandler<?,?> handler);

    void unRegisterJsHandler(String actionName);

    void callJsFunction(String actionName, Object params, CallBackFunction<?> callBackFunction);
}
```

实例: 

```java
getJsBridge().registerJsHandler("getUserName", new JsHandler<TestReqBean, String>() {
    @Override
    public ResponsePack<String> handle(TestReqBean req) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResponsePack<String> responsePack = new ResponsePack<>();
        responsePack.setData("小" + req.toString());
        return responsePack;
    }
});

getJsBridge().callJsFunction("getColorNeedTime", null, new CallBackFunction<TestJsonBean>() {
    @Override
    public void onCall(ResponsePack<TestJsonBean> response) {
        Toast.makeText(getApplicationContext(), response.getData().toString(), Toast.LENGTH_LONG).show();
    }
});
```
