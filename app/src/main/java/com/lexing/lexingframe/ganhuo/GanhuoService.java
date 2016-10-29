package com.lexing.lexingframe.ganhuo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;

/**
 * Author: mopel
 * Date : 2016/10/27
 */
public interface GanhuoService {

    @GET("Android/{size}/{index}")
    Single<GanhuoData<List<GanhuoAndroid>>> fetchAndroidGanhuo(
            @Path("size") int pageSize, @Path("index") int pageIndex);
}
