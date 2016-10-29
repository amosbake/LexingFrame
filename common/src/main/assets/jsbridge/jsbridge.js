/**
 * 协议：jsbridge://[action]/[params]/[callback_id]
 * 请求体标准json，响应体格式为{"errorCode":0,"errorMsg":"success","data":{}}
 */
(function (window) {
    if (window.JsBridge) {
        return;
    }
    var callbackMap = {};
    var handlerMap = {};

    function generateUUID() {
        return 'id' + new Date().getTime();
    }

    function registerHandler(action, callback) {
        if (action === 'return') {
            throw new Error('you can`t use return for your action name');
        }
        if (handlerMap[action]) {
            console.warn('there is already has a handler named' + action);
        }
        if (!callback || !(typeof callback === 'function')) {
            throw new Error('you have to register a callback function');
        }
        handlerMap[action] = callback;
    }

    function unregisterHandler(action) {
        if (handlerMap[action]) {
            delete handlerMap[action];
        }
    }

    //给native调用的
    function onReceive(action, params, callbackId) {
        if (action === 'return') {
            //native向js传递数据,实现js调native的完整流程
            var callback = callbackMap[callbackId];
            if (callback && typeof callback === 'function') {
                callback(JSON.parse(params));
                delete callbackMap[callbackId];
            }
        } else {
            //native调用js功能
            var handler = handlerMap[action];
            if (handler && typeof handler === 'function') {
                handler(JSON.parse(params), {
                    "send": function (result) {
                        if (!(result instanceof ResponsePack)) {
                            throw 'you have to send JsBridge.Response object in your handler ' + action;
                        }
                        location.href = "jsbridge://return/" + JSON.stringify(result) + "/" + callbackId;
                    }
                });
            }
        }
    }

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

    function callNative(action, params, callback) {
        if (action === 'return') {
            throw 'do`t use return for your action name';
        }
        params = JSON.stringify(params);
        var callbackId = generateUUID();
        callbackMap[callbackId] = callback;
        location.href = "jsbridge://" + action + "/" + params + "/" + callbackId;
    }

    window.JsBridge = {
        "registerHandler": registerHandler,
        "unregisterHandler": unregisterHandler,
        "callNative": callNative,
        "onReceive": onReceive,
        "Response": Response
    }
})(window);