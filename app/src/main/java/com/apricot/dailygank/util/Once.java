package com.apricot.dailygank.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Apricot on 2016/5/21.
 */
public class Once {

    SharedPreferences mSharedPreferences;
    Context mContex;

    public Once(Context context){
        mContex=context;
        mSharedPreferences=context.getSharedPreferences("once",Context.MODE_PRIVATE);
    }

    public void show(String tagKey,OnceCallback callback){
        boolean isSecondTime=mSharedPreferences.getBoolean(tagKey,false);
        if(!isSecondTime){
            callback.onOnce();
            SharedPreferences.Editor editor=mSharedPreferences.edit();
            editor.putBoolean(tagKey,true);
            editor.apply();
        }
    }

    public void show(int tagKetRes,OnceCallback callback){
        show(mContex.getString(tagKetRes),callback);
    }

    public interface OnceCallback{
        void onOnce();
    }
}
