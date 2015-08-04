package com.badou.mworking.presenter.category;

import com.badou.mworking.entity.category.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by badou1 on 2015/8/4.
 */
public class PlanDetailsPresenter {
    List<String> rids;
    //无效
    private List<String> getPlanInfoList(final List<Category> planList) {
       rids = new ArrayList<>();
        for (Category category :planList) {
            rids.add(category.getRid());
        }
        return rids;

    }




}
