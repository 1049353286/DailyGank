package com.apricot.dailygank.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Apricot on 2016/5/24.
 */
public class RxMeizi {
    public static Observable<Uri> saveImageAndGetPathObservable(final Context context, final String url, final String title){
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap=null;
                try{
                    bitmap= Picasso.with(context).load(url).get();
                }catch (IOException e){
                    subscriber.onError(e);
                }
                if(bitmap==null){
                    subscriber.onError(new Exception("无法下载到图片"));
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<Bitmap, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(Bitmap bitmap) {
                File appDir=new File(Environment.getExternalStorageDirectory(),"Meizi");
                if(!appDir.exists()){
                    appDir.mkdir();
                }
                String fileName=title.replace("/","-")+".jpg";
                File file=new File(appDir,fileName);
                try{
                    FileOutputStream outputStream=new FileOutputStream(file);
                    assert bitmap!=null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 //通知图库更新
                Uri uri=Uri.fromFile(file);
                Intent scannerIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri);
                context.sendBroadcast(scannerIntent);
                return Observable.just(uri);

            }
        }).subscribeOn(Schedulers.io());
    }
}