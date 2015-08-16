package com.badou.mworking.entity.category;

/**
 * Created by badou1 on 2015/7/28.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Plan extends Category {
    @Expose
    @SerializedName("stage")//计划阶段
            String stage;
    public String getStage() {
        return stage;
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