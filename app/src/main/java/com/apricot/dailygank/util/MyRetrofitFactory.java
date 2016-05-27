package com.apricot.dailygank.util;

/**
 * Created by Apricot on 2016/5/12.
 */
public class MyRetrofitFactory {
    public static final int meiziSize=10;
    public static final int gankSize=5;
    static GankApi GankIOSingleton=null;

    public static GankApi getGankIOSingleton(){
        if(GankIOSingleton==null){
            GankIOSingleton=new MyRetrofit().getGankService();
        }
        return GankIOSingleton;
    }
}
