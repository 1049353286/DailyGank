package com.apricot.dailygank.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Apricot on 2016/5/18.
 */
public class Shares {
    public static void share(Context context,String extraText){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT,extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent,"分享"));
    }

    public static void share(Context context,int StringRes){
        share(context, context.getString(StringRes));
    }

    public static void shareImage(Context context,Uri uri,String title){
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(shareIntent,title));
    }
}
