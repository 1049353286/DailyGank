package com.apricot.dailygank.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.apricot.dailygank.ui.GankFragment;
import com.apricot.dailygank.util.MyRetrofitFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Apricot on 2016/5/14.
 */
public class GankPagerAdapter extends FragmentPagerAdapter{

    Date mDate;

    public GankPagerAdapter(FragmentManager fm,Date date) {
        super(fm);
        mDate=date;
    }

    @Override
    public Fragment getItem(int position) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(mDate);
        calendar.add(Calendar.DATE,-position);
        return GankFragment.newInstance(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int getCount() {
        return MyRetrofitFactory.gankSize;
    }
}
