package com.apricot.dailygank.ui.base;

import android.support.v7.app.AppCompatActivity;

import com.apricot.dailygank.util.GankApi;
import com.apricot.dailygank.util.MyRetrofitFactory;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Apricot on 2016/5/10.
 */
public class BaseActivity extends AppCompatActivity{
    public static final GankApi GankIO= MyRetrofitFactory.getGankIOSingleton();
    private CompositeSubscription compositeSubscription;

    public void addSubscription(Subscription s){
        if(compositeSubscription==null){
            compositeSubscription=new CompositeSubscription();
        }
        compositeSubscription.add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(compositeSubscription!=null){
            compositeSubscription.unsubscribe();
        }
    }
}
