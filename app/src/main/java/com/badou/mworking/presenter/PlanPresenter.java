package com.badou.mworking.presenter;

import android.content.Context;

import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.presenter.category.CategoryBasePresenter;
import com.badou.mworking.util.GsonUtil;
import com.badou.mworking.view.BaseView;
import com.badou.mworking.view.PlanView;

/**
 * Created by badou1 on 2015/7/30.
 */
public class PlanPresenter  extends CategoryBasePresenter {

    PlanIntroductionPresenter mPlanIntroductionPresenter;
    PlanOperationPresenter mPlanOperationPresenter;
    CommentPresenter mCommentPresenter;
    PlanView mPlanView;


    public PlanPresenter(Context context, String rid) {
        super(context, Category.CATEGORY_ENTRY, rid);
    }

    @Override
    public void attachView(BaseView v) {
        super.attachView(v);
        mPlanView = (PlanView) v;
    }

    public void setChildPresenters(PlanIntroductionPresenter planIntroductionPresenter, PlanOperationPresenter planOperationPresenter, CommentPresenter commentPresenter) {
        this.mPlanIntroductionPresenter =planIntroductionPresenter;
        this.mPlanOperationPresenter = planOperationPresenter;
        this.mCommentPresenter = commentPresenter;
    }

    @Override
    public void setData(CategoryDetail categoryDetail) {
        GsonUtil.toJson("in detail: "+categoryDetail);
        super.setData(categoryDetail);
        mPlanIntroductionPresenter.setData(categoryDetail);
        mPlanOperationPresenter.setData(categoryDetail); // 这个打错了
        if (categoryDetail.getEntry().getIn() == 2) {
            mPlanView.setSwipeEnable(true);
        } else {
            mPlanView.setSwipeEnable(false);
        }
    }

    @Override
    public boolean onBackPressed() {
        return mCommentPresenter.onBackPressed();
    }
}
