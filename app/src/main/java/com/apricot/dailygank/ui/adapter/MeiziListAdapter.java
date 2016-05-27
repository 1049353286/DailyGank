package com.apricot.dailygank.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apricot.dailygank.R;
import com.apricot.dailygank.data.entity.Meizi;
import com.apricot.dailygank.widget.RatioImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Apricot on 2016/5/11.
 */
public class MeiziListAdapter extends RecyclerView.Adapter<MeiziListAdapter.ViewHolder>{
    private List<Meizi> meizis;
    OnMeiziTouchListener onMeiziTouchListener;
    private Context mContext;

    public MeiziListAdapter(Context context,List<Meizi> meizis){
        mContext=context;
        this.meizis=meizis;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meizi,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meizi meizi=meizis.get(position);
        holder.meizi=meizi;
        holder.textView.setText(meizi.desc);
        holder.card.setTag(meizi.desc);
        Glide.with(mContext)
                .load(meizi.url)
                .centerCrop()
                .into(holder.imageView)
                .getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(int width, int height) {
                        if(!holder.card.isShown()){
                            holder.card.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return meizis.size();
    }

    public interface OnMeiziTouchListener{
        void onTouch(View v, View meizhiView, View card, Meizi meizhi);
    }

    public void setOnMeiziTouchListener(OnMeiziTouchListener listener){
        onMeiziTouchListener=listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.iv_meizi)
        RatioImageView imageView;
        @Bind(R.id.tv_title)
        TextView textView;
        View card;
        Meizi meizi;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView.setOnClickListener(this);
            imageView.setOriginalSize(50,50);
            card=itemView;
            card.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onMeiziTouchListener.onTouch(v,imageView,card,meizi);
        }
    }
}
