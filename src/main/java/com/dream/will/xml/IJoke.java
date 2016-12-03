package com.dream.will.xml;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Author：Will on 2016/12/2 09:49
 * Mail：heheheqin.will@gmail.com
 */

public interface IJoke {
    @GET("/8hr/page/{page}/?s=4935472")
    Call<String> getData(@Path("page") int s);

    @GET("/article/{hrefs}")
    Call<String> getConmment(@Path("hrefs") String s);
}
