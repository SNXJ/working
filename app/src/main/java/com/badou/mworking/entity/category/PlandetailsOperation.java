package com.badou.mworking.entity.category;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SNXJ on 2015/8/16.
 */
public class PlandetailsOperation {
    @SerializedName("rid")
    String rid;
    @SerializedName("categoryDetail")
    CategoryDetail categoryDetail;

    public PlandetailsOperation(String rid, CategoryDetail categoryDetail) {
        this.rid = rid;
        this.categoryDetail = categoryDetail;
    }

    public String getRid() {
        return rid;
    }

    public CategoryDetail getCategoryDetail() {
        return categoryDetail;
    }

    public void setCategoryDetail(CategoryDetail categoryDetail) {
        this.categoryDetail = categoryDetail;
    }
}
