package com.apricot.dailygank.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.apricot.dailygank.R;
import com.apricot.dailygank.data.GankData;
import com.apricot.dailygank.data.entity.Gank;
import com.apricot.dailygank.ui.adapter.GankListAdapter;
import com.apricot.dailygank.ui.base.BaseActivity;
import com.apricot.dailygank.util.Shares;
import com.apricot.dailygank.widget.VideoImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Apricot on 2016/5/14.
 */
public class    GankFragment extends Fragment{
    private static final String TAG="GankFragment";
    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";
    @Bind(R.id.rv_gank)
    RecyclerView mRecyclerView;

    @Bind(R.id.stub_empty_view)
    ViewStub mEmptyViewStub;
//    @Bind(R.id.stub_video_view)
//    ViewStub mVideoViewStub;
    @Bind(R.id.iv_video)
    VideoImageView mVideoImageView;

    int mYear, mMonth, mDay;
    List<Gank> mGankList;
    GankListAdapter mAdapter;
    Subscription mSubscription;

    public static GankFragment newInstance(int year, int month, int day){
        GankFragment fragment=new GankFragment();
        Bundle args=new Bundle();
        args.putInt(ARG_YEAR,year);
        args.putInt(ARG_MONTH,month);
        args.putInt(ARG_DAY,day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGankList=new ArrayList<>();
        mAdapter=new GankListAdapter(mGankList);
        Bundle bundle = getArguments();
        mYear = bundle.getInt(ARG_YEAR);
        mMonth = bundle.getInt(ARG_MONTH);
        mDay = bundle.getInt(ARG_DAY);
        setHasOptionsMenu(true);  //可在fragment中写onOptionsItemSelected
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_gank, container, false);
        ButterKnife.bind(this, v);
        initRecycleView();
        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mGankList.size()==0){
            loadData();
        }
    }

    private void initRecycleView(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    private void loadData(){
        mSubscription= BaseActivity.GankIO.getGankData(mYear,mMonth,mDay)
                .map(new Func1<GankData, GankData.Result>() {
                    @Override
                    public GankData.Result call(GankData gankData) {
                        return gankData.results;
                    }
                })
                .map(new Func1<GankData.Result, List<Gank>>() {
                    @Override
                    public List<Gank> call(GankData.Result result) {
                        return addAllResults(result);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Gank>>() {
                    @Override
                    public void call(List<Gank> ganks) {
                        if(ganks.isEmpty()){
                            showEmptyView();
                        }else{
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void showEmptyView(){
        mEmptyViewStub.inflate();
    }

    private List<Gank> addAllResults(GankData.Result results) {
        if (results.androidList != null) mGankList.addAll(results.androidList);
        if (results.iOSList != null) mGankList.addAll(results.iOSList);
        if (results.appList != null) mGankList.addAll(results.appList);
        if (results.拓展资源List != null) mGankList.addAll(results.拓展资源List);
        if (results.瞎推荐List != null) mGankList.addAll(results.瞎推荐List);
        if (results.休息视频List != null) mGankList.addAll(0, results.休息视频List);
        return mGankList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                if (mGankList.size() != 0) {
                    Gank gank = mGankList.get(0);
                    String shareText = gank.desc + gank.url;
                    Shares.share(getActivity(), shareText);
                } else {
                    Shares.share(getContext(), R.string.share_text);
                }
                return true;
            case R.id.action_subject:
                openTodaySubject();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openTodaySubject(){
        String url=getString(R.string.gank_url)+String.format("%s/%s/%s", mYear, mMonth, mDay);
        WebActivity.StartWebActivity(getActivity(),url,getString(R.string.action_subject));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSubscription!=null){
            mSubscription.unsubscribe();
        }
    }
}
