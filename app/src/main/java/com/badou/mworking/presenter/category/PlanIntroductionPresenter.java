package com.badou.mworking.presenter.category;

import android.content.Context;

import com.badou.mworking.domain.category.EnrollUseCase;
import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.presenter.Presenter;
import com.badou.mworking.view.BaseView;
import com.badou.mworking.view.PlanIntroductionView;

/**
 * Created by badou1 on 2015/7/30.
 */
public class PlanIntroductionPresenter  extends Presenter {

    PlanIntroductionView mPlanIntroductionView;
    CategoryDetail mCategoryDetail;
    String mRid;

    public PlanIntroductionPresenter(Context context, String rid) {
        super(context);
        this.mRid = rid;
    }

    @Override
    public void attachView(BaseView v) {
        mPlanIntroductionView = (PlanIntroductionView) v;
    }

    public void setData(CategoryDetail categoryDetail) {
        mPlanIntroductionView.setData(categoryDetail);
        this.mCategoryDetail = categoryDetail;
    }
}
