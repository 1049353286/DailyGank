package com.apricot.dailygank.data;

import com.apricot.dailygank.data.entity.Gank;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Apricot on 2016/5/10.
 */
public class GankData extends BaseData{
    public Result results;
    public List<String> category;
    public class Result{
        @SerializedName("Android") public List<Gank> androidList;
        @SerializedName("休息视频") public List<Gank> 休息视频List;
        @SerializedName("iOS") public List<Gank> iOSList;
        @SerializedName("福利") public List<Gank> 妹纸List;
        @SerializedName("拓展资源") public List<Gank> 拓展资源List;
        @SerializedName("瞎推荐") public List<Gank> 瞎推荐List;
        @SerializedName("App") public List<Gank> appList;
    }
}
