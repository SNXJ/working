package com.badou.mworking;

import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.category.CategoryDetail;

/**
 * Created by badou1 on 2015/8/4.
 */
public class PlanCategory  extends Category {



    @Override
    public int getCategoryType() {
        return 0;
    }

    @Override
    public void updateData(CategoryDetail categoryDetail) {

    }
}
