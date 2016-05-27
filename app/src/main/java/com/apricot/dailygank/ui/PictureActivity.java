package com.apricot.dailygank.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.apricot.dailygank.R;
import com.apricot.dailygank.ui.base.ToolBarActivity;
import com.apricot.dailygank.util.RxMeizi;
import com.apricot.dailygank.util.Shares;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Apricot on 2016/5/24.
 */
public class PictureActivity extends ToolBarActivity{
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    @Bind(R.id.picture)
    ImageView mImageView;

    PhotoViewAttacher mPhotoViewAttacher;
    String mImageUrl,mImageTitle;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_picture;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    public static void startPictureActivity(Context context,String url,String title){
        Intent intent=new Intent(context,PictureActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, url);
        intent.putExtra(EXTRA_IMAGE_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mImageUrl=getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mImageTitle=getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
        setTitle(mImageTitle);
        Picasso.with(this).load(mImageUrl).into(mImageView);
        setupPhotoAttacher();
    }


    private void setupPhotoAttacher(){
        mPhotoViewAttacher =new PhotoViewAttacher(mImageView);
        mPhotoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(PictureActivity.this)
                        .setMessage("保存到手机？")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveImageToGallery();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            }
        });
        mPhotoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                hideOrShowToolbar();
            }
        });
    }

    private void saveImageToGallery(){
        Subscription s=RxMeizi.saveImageAndGetPathObservable(this, mImageUrl, mImageTitle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        File appDir = new File(Environment.getExternalStorageDirectory(), "Meizi");
                        String msg = String.format("图片已保存至%s文件夹", appDir.getAbsolutePath());
                        Toast.makeText(PictureActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(PictureActivity.this,throwable.getMessage()+"\n再试试...",Toast.LENGTH_LONG).show();
                    }
                });
        addSubscription(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                RxMeizi.saveImageAndGetPathObservable(this, mImageUrl, mImageTitle)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Uri>() {
                            @Override
                            public void call(Uri uri) {
                                Shares.shareImage(PictureActivity.this, uri, getString(R.string.share_meizhi_to));
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(PictureActivity.this,throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                return true;
            case R.id.action_save:
                saveImageToGallery();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoViewAttacher.cleanup();
        ButterKnife.unbind(this);
    }
}
