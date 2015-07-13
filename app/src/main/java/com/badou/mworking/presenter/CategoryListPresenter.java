package com.badou.mworking.presenter;

import android.content.Context;

import com.badou.mworking.domain.CategoryUseCase;
import com.badou.mworking.domain.ClassificationUseCase;
import com.badou.mworking.domain.UseCase;
import com.badou.mworking.entity.category.CategoryOverall;
import com.badou.mworking.entity.category.Classification;
import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.category.Exam;
import com.badou.mworking.entity.category.Notice;
import com.badou.mworking.entity.category.Task;
import com.badou.mworking.entity.category.Train;
import com.badou.mworking.net.BaseListSubscriber;
import com.badou.mworking.util.CategoryClickHandler;
import com.badou.mworking.util.SPHelper;
import com.badou.mworking.view.BaseView;
import com.badou.mworking.view.CategoryListView;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryListPresenter extends ListPresenter<Category> {
    protected final int REQUEST_DETAIL = 1;

    protected CategoryListView mCategoryListView;
    protected CategoryUseCase mCategoryUseCase;

    protected final int mCategoryIndex;
    protected boolean isUnread = false;
    protected boolean status_menu_show = false;

    public CategoryListPresenter(Context context, int category) {
        super(context);
        this.mCategoryIndex = category;
    }

    @Override
    public void attachView(BaseView v) {
        mCategoryListView = (CategoryListView) v;
        getClassifications();
        super.attachView(v);
    }

    @Override
    protected Type getType() {
        switch (mCategoryIndex) {
            case Category.CATEGORY_NOTICE:
                return new TypeToken<List<Notice>>() {
                }.getType();
            case Category.CATEGORY_TRAINING:
                return new TypeToken<List<Train>>() {
                }.getType();
            case Category.CATEGORY_EXAM:
                return new TypeToken<List<Exam>>() {
                }.getType();
            case Category.CATEGORY_TASK:
                return new TypeToken<List<Task>>() {
                }.getType();
            case Category.CATEGORY_SHELF:
                return new TypeToken<List<Train>>() {
                }.getType();
        }
        return new TypeToken<List<Notice>>() {
        }.getType();
    }

    @Override
    protected UseCase getLoadMoreUseCase() {
        if (mCategoryUseCase == null)
            mCategoryUseCase = new CategoryUseCase(mCategoryIndex);
        mCategoryUseCase.setPageNum(mCurrentIndex + 1);
        return mCategoryUseCase;
    }

    @Override

    protected UseCase getRefreshUseCase() {
        if (mCategoryUseCase == null)
            mCategoryUseCase = new CategoryUseCase(mCategoryIndex);
        mCategoryUseCase.setPageNum(1);
        return mCategoryUseCase;
    }

    @Override
    protected String getCacheKey() {
        return Category.CATEGORY_KEY_UNREADS[mCategoryIndex];
    }

    @Override
    protected boolean setData(Object data, int index) {
        CategoryOverall categoryOverall = (CategoryOverall) data;
        SPHelper.setUnreadNumber(mCategoryIndex, categoryOverall.getUnreadCount());
        return setList(categoryOverall.getCategoryList(mCategoryIndex), index);
    }

    @Override
    protected void toDetailPage(Category category) {
        mActivity.startActivityForResult(CategoryClickHandler.getIntent(mContext, category), REQUEST_DETAIL);
        setRead(category);
    }


    // 功能描述: 设置已读
    protected void setRead(Category category) {
        if (category.isUnread()) {
            SPHelper.reduceUnreadNumberByOne(mCategoryIndex);
        }
    }

    // 功能描述:通过网络获取 类别 列表
    private void getClassifications() {
        mCategoryListView.setMainClassification(SPHelper.getClassification(Category.CATEGORY_KEY_NAMES[mCategoryIndex]));
        new ClassificationUseCase(Category.CATEGORY_KEY_NAMES[mCategoryIndex]).execute(new BaseListSubscriber<Classification>(mContext) {

            @Override
            public void onResponseSuccess(List<Classification> data) {
                Collections.sort(data, new Comparator<Classification>() {
                    @Override
                    public int compare(Classification lhs, Classification rhs) {
                        return -Integer.valueOf(lhs.getPriority()).compareTo(rhs.getPriority());
                    }
                });
                SPHelper.setClassification(Category.CATEGORY_KEY_NAMES[mCategoryIndex], data);
                mCategoryListView.setMainClassification(data);
            }
        });
    }

    public void onClassificationStatusChanged() {
        if (status_menu_show) {
            mCategoryListView.hideMenu();
        } else {
            mCategoryListView.showMenu();
        }
        status_menu_show = !status_menu_show;
    }

    public void onClassificationMainClicked(Classification classification) {
        mCategoryListView.setMoreClassification(classification.getSon());
        if (classification.getSon() == null || classification.getSon().size() == 0) {
            mCategoryListView.hideMenu();
            status_menu_show = false;
            mCategoryUseCase.setTag(classification.getTag());
            mCategoryListView.refreshComplete();
            mCategoryListView.setRefreshing();
            mCategoryListView.setActionbarTitle(classification.getName());
        }
    }

    public void onClassificationMoreClicked(Classification classification) {
        mCategoryListView.hideMenu();
        status_menu_show = false;
        mCategoryUseCase.setTag(classification.getTag());
        mCategoryListView.refreshComplete();
        mCategoryListView.startRefreshing();
        mCategoryListView.setActionbarTitle(classification.getName());
    }

    public void onUnreadClick() {
        isUnread = !isUnread;
        mCategoryListView.setUnread(isUnread);
        mCategoryUseCase.setDone(isUnread ? CategoryUseCase.TYPE_UNREAD : CategoryUseCase.TYPE_ALL);
        mCategoryListView.refreshComplete();
        mCategoryListView.startRefreshing();
        mCategoryListView.hideMenu();
        status_menu_show = false;
    }
}