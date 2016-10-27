package com.lexing.lexingframe.core;

import com.lexing.common.assist.L;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Asura on 2016/10/9.
 */

public class LogInterceptor implements Interceptor {
    private final Charset charset = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Buffer buffer = new Buffer();
        RequestBody body = request.body();
        if (null != body) {
            body.writeTo(buffer);
            MediaType contentType = body.contentType();
            if (contentType != null) {
                contentType.charset(charset);
            }
        }
        L.i(request.method() + ": " + request.url().toString() + " \nbody: " + buffer.readString(charset));
        Response response = chain.proceed(request);
        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        buffer = source.buffer();
        Buffer clone = buffer.clone();
        String json = clone.readString(charset);
        L.i(json);
        return response;
    }
}
