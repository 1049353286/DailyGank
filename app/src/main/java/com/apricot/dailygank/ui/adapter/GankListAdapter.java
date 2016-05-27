package com.apricot.dailygank.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apricot.dailygank.R;
import com.apricot.dailygank.data.entity.Gank;
import com.apricot.dailygank.ui.WebActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Apricot on 2016/5/14.
 */
public class GankListAdapter extends RecyclerView.Adapter<GankListAdapter.ViewHolder>{

    private List<Gank> mGankList;

    public GankListAdapter(List<Gank> ganks){
        mGankList=ganks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gank gank=mGankList.get(position);
        if(position==0){
            showCategory(holder);
        }else{
            boolean theLastCategoryEqualsToThis=mGankList.get(position-1).type.equals(mGankList.get(position).type);
            if(!theLastCategoryEqualsToThis){
                showCategory(holder);
            }else{
                hideCategory(holder);
            }
        }
        holder.category.setText(gank.type);
        holder.gank.setText(gank.desc+"via:"+gank.who);

    }

    private void showCategory(ViewHolder holder) {
        if (!isVisibleOf(holder.category)) holder.category.setVisibility(View.VISIBLE);
    }


    private void hideCategory(ViewHolder holder) {
        if (isVisibleOf(holder.category)) holder.category.setVisibility(View.GONE);
    }

    private boolean isVisibleOf(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getItemCount() {
        return mGankList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_category)
        TextView category;
        @Bind(R.id.tv_title)
        TextView gank;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.ll_gank_parent) void onGank(View v){
            Gank gank=mGankList.get(getLayoutPosition());
            WebActivity.StartWebActivity(v.getContext(),gank.url,gank.desc);
        }
    }
}
