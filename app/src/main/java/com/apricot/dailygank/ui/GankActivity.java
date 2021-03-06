package com.apricot.dailygank.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.apricot.dailygank.R;
import com.apricot.dailygank.ui.adapter.GankPagerAdapter;
import com.apricot.dailygank.ui.base.ToolBarActivity;
import com.apricot.dailygank.util.Dates;


import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Apricot on 2016/5/11.
 */
public class GankActivity extends ToolBarActivity implements ViewPager.OnPageChangeListener {
    public static final String EXTRA_GANK_DATE = "gank_date";
    @Bind(R.id.pager)
    ViewPager mViewPager;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    GankPagerAdapter mPagerAdapter;
    Date mGankDate;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_gank;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mGankDate= (Date) getIntent().getSerializableExtra(EXTRA_GANK_DATE);
        setTitle(Dates.toDate(mGankDate));
        initViewPager();
        initTabLayout();

    }

    private void initViewPager(){
        mPagerAdapter=new GankPagerAdapter(getSupportFragmentManager(),mGankDate);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);

    }

    private void initTabLayout() {
        for(int i=0;i<mPagerAdapter.getCount();i++){
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gank,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
        ButterKnife.unbind(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitle(Dates.toDate(mGankDate,-position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
