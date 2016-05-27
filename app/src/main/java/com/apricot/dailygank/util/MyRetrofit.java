package com.apricot.dailygank.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Apricot on 2016/5/10.
 */
public class MyRetrofit {
    private GankApi gankService;

    private Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    MyRetrofit(){
        OkHttpClient client=new OkHttpClient();
        client.setReadTimeout(12, TimeUnit.SECONDS);

        RestAdapter.Builder builder=new RestAdapter.Builder();
        builder.setClient(new OkClient(client))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://gank.io/api")
                .setConverter(new GsonConverter(gson));
        RestAdapter gankRestAdapter=builder.build();
        gankService=gankRestAdapter.create(GankApi.class);
    }

    public  GankApi getGankService(){
        return gankService;
    }
}
