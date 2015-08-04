package com.badou.mworking.entity.category;

/**
 * Created by badou1 on 2015/7/28.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Plan extends Category {

    @Expose
    @SerializedName("plan")//
   String plan;

    public String getPlan (){
        return plan;
    }


    @Expose
    @SerializedName("config")//課程計劃描述
         String config;

    @Expose
    @SerializedName("now")//課程計劃描述
            String now;


    //學習計劃描述
    public String getConfig() {
        return config;
    }

    public String getPlanNow() {
        return now;
    }


    @Override
    public int getCategoryType() {
        return Category.CATEGORY_PLAN;
    }

    @Override
    public void updateData(CategoryDetail categoryDetail) {
        this.store = categoryDetail.store;
      //  this.read = categoryDetail.entry.in;
    }

      public int getRead() {
        return read;
    }
}