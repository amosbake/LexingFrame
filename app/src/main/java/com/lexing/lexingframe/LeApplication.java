package com.lexing.lexingframe;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.lexing.common.base.BaseApplication;
import com.lexing.lexingframe.core.ServiceGenerator;
import com.squareup.picasso.Picasso;

/**
 * Author: mopel
 * Date : 2016/10/27
 */
public class LeApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Picasso _picasso = new Picasso.Builder(this)
                .downloader( new OkHttp3Downloader(ServiceGenerator.getOkHttpClient()))
                .build();
        try {
            Picasso.setSingletonInstance(_picasso);
        } catch (IllegalStateException ignored) {
        }
    }
}
