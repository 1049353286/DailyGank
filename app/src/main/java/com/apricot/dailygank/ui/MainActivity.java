package com.apricot.dailygank.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apricot.dailygank.App;
import com.apricot.dailygank.R;
import com.apricot.dailygank.data.MeiziData;
import com.apricot.dailygank.data.entity.Meizi;
import com.apricot.dailygank.ui.adapter.MeiziListAdapter;
import com.apricot.dailygank.ui.base.BaseActivity;
import com.apricot.dailygank.ui.base.ToolBarActivity;
import com.apricot.dailygank.util.Once;
import com.apricot.dailygank.util.PreferencesLoader;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Apricot on 2016/5/10.
 */
public class MainActivity extends ToolBarActivity implements SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG="MainActivity";
    @Bind(R.id.rv_meizi)
    RecyclerView mRecycleView;
    private MeiziListAdapter mMeiziListAdapter;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Meizi> mMeiziList;
    private int page=1;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mMeiziList=new ArrayList<>();
        QueryBuilder query=new QueryBuilder(Meizi.class);
        query.appendOrderDescBy("publishedAt");
        query.distinct(true);
        query.limit(0, 10);
        mMeiziList.addAll(App.DB.<Meizi>query(query));
        initRecycleView();
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mSwipeRefreshLayout.setRefreshing(true);
//        loadData(true);
//    }

    void initRecycleView(){
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(staggeredGridLayoutManager);
        mMeiziListAdapter=new MeiziListAdapter(this,mMeiziList);
        mRecycleView.setAdapter(mMeiziListAdapter);
        mMeiziListAdapter.setOnMeiziTouchListener(getOnMeiTouchListener());
        mRecycleView.addOnScrollListener(getOnScrollListener(staggeredGridLayoutManager));
        new Once(this).show("tip_guide_1", new Once.OnceCallback() {
            @Override
            public void onOnce() {
                Snackbar.make(mRecycleView,R.string.tip_guide,Snackbar.LENGTH_INDEFINITE)
                        .setAction("知道了", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        });


    }

    private RecyclerView.OnScrollListener getOnScrollListener(final StaggeredGridLayoutManager staggeredGridLayoutManager){
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    int [] lastVisibleItem=staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
                    int position=Math.max(lastVisibleItem[0],lastVisibleItem[1]);
                    if(position+1==mMeiziListAdapter.getItemCount()){
                        mSwipeRefreshLayout.setRefreshing(true);
                        page++;
                        loadData();
                    }
                }
            }
        };
    }

    private MeiziListAdapter.OnMeiziTouchListener getOnMeiTouchListener(){
        return new MeiziListAdapter.OnMeiziTouchListener() {
            @Override
            public void onTouch(View v, View meizhiView, View card, final Meizi meizhi) {
                if(meizhi==null)return;
                if(v==meizhiView){
                    Picasso.with(MainActivity.this).load(meizhi.url).fetch(new Callback() {
                        @Override
                        public void onSuccess() {
                            PictureActivity.startPictureActivity(MainActivity.this,meizhi.url,meizhi.desc);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }else if(v==card){
                    startGankActivity(meizhi.publishedAt);
                }
            }
        };
    }

    public void startGankActivity(Date publishedAt){
        Intent intent=new Intent(this,GankActivity.class);
        intent.putExtra(GankActivity.EXTRA_GANK_DATE,publishedAt);
        startActivity(intent);
    }

    private void loadData(){
        loadData(false);
    }

    private void loadData(final boolean clean){
        Subscription s= BaseActivity.GankIO.getMeiziData(page)
                .map(new Func1<MeiziData, List<Meizi>>() {
                    @Override
                    public List<Meizi> call(MeiziData meiziData) {
                        return meiziData.results;
                    }
                })
//                .flatMap(new Func1<List<Meizi>, Observable<Meizi>>() {
//                    @Override
//                    public Observable<Meizi> call(List<Meizi> meizis) {
//                        return Observable.from(meizis);
//                    }
//                })
//                .toSortedList(new Func2<Meizi, Meizi, Integer>() {
//                    @Override
//                    public Integer call(Meizi meizi, Meizi meizi2) {
//                        return meizi2.publishedAt.compareTo(meizi.publishedAt);
//                    }
//                })
//                .doOnNext(new Action1<List<Meizi>>() {
//                    @Override
//                    public void call(List<Meizi> meizis) {
//                        saveMeizis(meizis);
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                })
                .subscribe(new Action1<List<Meizi>>() {
                    @Override
                    public void call(List<Meizi> meizis) {
                        if (clean) {
                            mMeiziList.clear();
                        }
                        mMeiziList.addAll(meizis);
                        mMeiziListAdapter.notifyDataSetChanged();
                        saveMeizis(meizis);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadError(throwable);
                    }
                });
        addSubscription(s);


//        mLastVideoIndex = 0;
//        // @formatter:off
//        Subscription s = Observable
//                .zip(sGankIO.getMeizhiData(mPage),
//                        sGankIO.get休息视频Data(mPage),
//                        this::createMeizhiDataWith休息视频Desc)
//                .map(meizhiData -> meizhiData.results)
//                .flatMap(Observable::from)
//                .toSortedList((meizhi1, meizhi2) ->
//                        meizhi2.publishedAt.compareTo(meizhi1.publishedAt))
//                .doOnNext(this::saveMeizhis)
//                .observeOn(AndroidSchedulers.mainThread())
//                .finallyDo(() -> setRefresh(false))
//                .subscribe(meizhis -> {
//                    if (clean) mMeizhiList.clear();
//                    mMeizhiList.addAll(meizhis);
//                    mMeizhiListAdapter.notifyDataSetChanged();
//                    setRefresh(false);
//                }, throwable -> loadError(throwable));
//        // @formatter:on
//        addSubscription(s);
    }

    private void saveMeizis(final List<Meizi> meizis){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Meizi> oldMeiziList=new ArrayList<Meizi>();
                List<Meizi> newMeiziList=new ArrayList<Meizi>();
                QueryBuilder query=new QueryBuilder(Meizi.class);
                query.appendOrderDescBy("publishedAt");
                query.limit(0, 10);
                oldMeiziList.addAll(App.DB.<Meizi>query(query));
                if(oldMeiziList.size()<1) {
                    Log.e(TAG, "save meizi");
                    App.DB.save(meizis);
                }
                if(oldMeiziList!=meizis&&oldMeiziList.size()>0){
                    Meizi lastMeizi=oldMeiziList.get(0);
                    for (int i=meizis.size()-1;i>=0;i--){
                        if(lastMeizi.publishedAt.equals(meizis.get(i).publishedAt)&&i!=0){
                            newMeiziList.addAll(meizis.subList(0,i));
                            break;
                        }
                    }
                    Log.e(TAG,"update meizi");
                    App.DB.insert(newMeiziList,ConflictAlgorithm.Replace);
                }
            }
        }).start();
    }
    private void loadError(Throwable t){
        t.printStackTrace();
        Snackbar.make(mRecycleView,"加载失败请重试",Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.fab) public void onFab(){
        if(mMeiziList!=null&&mMeiziList.size()>0){
            startGankActivity(mMeiziList.get(0).publishedAt);
        }
    }

    @Override
    public void onToolbarClick() {
        mRecycleView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        page=1;
        mSwipeRefreshLayout.setRefreshing(true);
        loadData(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item=menu.findItem(R.id.action_notifiable);
        initNotifiableItemState(item);
        return true;
    }
    public void initNotifiableItemState(MenuItem item){
        PreferencesLoader loader=new PreferencesLoader(this);
        item.setChecked(loader.getBoolean(R.string.action_notifiable, true));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_trending:
                openGitHubTrending();
                return true;
            case R.id.action_notifiable:
                boolean isChecked=!item.isChecked();
                PreferencesLoader loader=new PreferencesLoader(this);
                loader.saveBoolean(R.string.action_notifiable, isChecked);
                item.setChecked(isChecked);
                Toast.makeText(this,isChecked?R.string.notifiable_on:R.string.notifiable_off,Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openGitHubTrending(){
        String url=getString(R.string.url_github_trending);
        String title=getString(R.string.action_github_trending);
        WebActivity.StartWebActivity(this,url,title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
