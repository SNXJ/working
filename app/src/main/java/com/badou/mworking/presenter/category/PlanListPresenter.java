package com.badou.mworking.presenter.category;

import android.content.Context;

import com.badou.mworking.domain.category.CategoryGetInfoUseCase;
import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.category.CategoryOverall;
import com.badou.mworking.entity.category.Plan;
import com.badou.mworking.net.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SNXJ on 2015/8/5.
 */
public class PlanListPresenter extends CategoryListPresenter {

    public static final String RESPONSE_RATING_NUMBER = "ratingNumber";
    public static final String RESPONSE_COMMENT_NUMBER = "commentNumber";

    public PlanListPresenter(Context context, int category) {
        super(context, category);
    }

    @Override
    protected boolean setData(Object data, int index) {
        CategoryOverall categoryOverall = (CategoryOverall) data;
        List<Category> trainingList = categoryOverall.getCategoryList(mCategoryIndex);
        updateCommentInfo(trainingList, index);
        return super.setData(data, index);
    }

    private void updateCommentInfo(final List<Category> trainingList, final int index) {
        List<String> rids = new ArrayList<>();
        for (Category category : trainingList) {
            rids.add(category.getRid());
        }

        new CategoryGetInfoUseCase(rids).execute(new BaseSubscriber<Plan>(mContext) {

            @Override
            public void onResponseSuccess(Plan data) {

            }
        });


    }
}
