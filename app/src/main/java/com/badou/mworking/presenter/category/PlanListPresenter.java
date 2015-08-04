package com.badou.mworking.presenter.category;

import android.content.Context;

import com.badou.mworking.domain.category.PlanCommentInfoUseCase;
import com.badou.mworking.domain.category.TrainingCommentInfoUseCase;
import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.category.CategoryOverall;
import com.badou.mworking.entity.category.Train;
import com.badou.mworking.net.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by badou1 on 2015/8/4.
 */
public class PlanListPresenter  extends CategoryListPresenter {



    PlanCommentInfoUseCase commentInfoUseCase = new PlanCommentInfoUseCase();

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

    private void updateCommentInfo(final List<Category> planingList, final int index) {
        List<String> rids = new ArrayList<>();
        for (Category category : planingList) {
            rids.add(category.getRid());
        }
        commentInfoUseCase.setRids(rids);
        commentInfoUseCase.execute(new BaseSubscriber<List<Train.TrainingCommentInfo>>(mContext) {
            @Override
            public void onResponseSuccess(List<Train.TrainingCommentInfo> data) {
                for (Train.TrainingCommentInfo commentInfo : data) {
                    for (Category category : planingList) {
                        if (category.getRid().equals(commentInfo.getRid())) {
                            ((Train) category).setCommentInfo(commentInfo);
                            PlanListPresenter.super.setList(planingList, index);
                        }
                    }
                }
            }

            @Override
            public void onCompleted() {
                mBaseListView.refreshComplete();
            }
        });
    }
}
