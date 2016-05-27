package com.apricot.dailygank;

import android.app.Application;
import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * Created by Apricot on 2016/5/10.
 */
public class App extends Application{
    private static final String DB_NAME="gank.db";
    public Context mContext;
    public static LiteOrm DB;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        DB=LiteOrm.newSingleInstance(mContext,DB_NAME);
        if(BuildConfig.DEBUG){
            DB.setDebugged(true);
        }

    }
}
