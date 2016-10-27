package com.lexing.lexingframe.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lexing.lexingframe.BuildConfig;
import com.lexing.lexingframe.ganhuo.GanhuoService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: mopel
 * Date : 2016/10/27
 */
public class ServiceGenerator {
    public static final String GANHUO_URL = "http://gank.io/api/data/"; //干货集中营api
    private static OkHttpClient sOkHttpClient;
    private static Retrofit.Builder sBuilder;
    private static Gson sGson;

    static {
        OkHttpClient.Builder _okHttpBuilder = new OkHttpClient.Builder();
        _okHttpBuilder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        if (BuildConfig.DEBUG){
            _okHttpBuilder.addInterceptor(new LogInterceptor());
        }
        sOkHttpClient = _okHttpBuilder.build();

        sGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        sBuilder = new Retrofit.Builder()
                .client(sOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(sGson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    public static GanhuoService ganhuoService(){
        return sBuilder.baseUrl(GANHUO_URL)
                .build()
                .create(GanhuoService.class);
    }

    public static OkHttpClient getOkHttpClient() {
        return sOkHttpClient;
    }

    public static Retrofit.Builder getBuilder() {
        return sBuilder;
    }

    public static Gson getGson() {
        return sGson;
    }
}
