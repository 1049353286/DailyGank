package com.apricot.dailygank.util;

import com.apricot.dailygank.data.GankData;
import com.apricot.dailygank.data.MeiziData;
import com.apricot.dailygank.data.RestVideoData;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
/**
 * Created by Apricot on 2016/5/10.
 */
public interface GankApi {
    @GET("/data/福利/" + MyRetrofitFactory.meiziSize + "/{page}")
    Observable<MeiziData> getMeiziData(
            @Path("page") int page);

    @GET("/day/{year}/{month}/{day}") Observable<GankData> getGankData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

    @GET("/data/休息视频/" + MyRetrofitFactory.meiziSize + "/{page}") Observable<RestVideoData> getRestVideoData(
            @Path("page") int page);
}
