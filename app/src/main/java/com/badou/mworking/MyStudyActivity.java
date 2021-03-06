package com.badou.mworking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.badou.mworking.adapter.UserProgressAdapter;
import com.badou.mworking.base.BaseNoTitleActivity;
import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.user.UserDetail;
import com.badou.mworking.presenter.Presenter;
import com.badou.mworking.presenter.UserProgressPresenter;
import com.badou.mworking.view.BaseListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyStudyActivity extends BaseNoTitleActivity implements BaseListView<Category> {

    private static final String KEY_USER_DETAIL = "userdetail";

    @Bind(R.id.back_image_view)
    ImageView mBackImageView;
    @Bind(R.id.description_text_view)
    TextView mDescriptionTextView;
    @Bind(R.id.rank_text_view)
    TextView mRankTextView;
    @Bind(R.id.content_list_view)
    PullToRefreshListView mContentListView;
    @Bind(R.id.bottom_button)
    TextView mBottomButton;

    UserProgressPresenter mPresenter;
    UserProgressAdapter mCategoryAdapter;

    public static Intent getIntent(Context context, UserDetail userDetail) {
        Intent intent = new Intent(context, MyStudyActivity.class);
        intent.putExtra(KEY_USER_DETAIL, userDetail);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_study);
        ButterKnife.bind(this);
        initView();
        initData((UserDetail) mReceivedIntent.getSerializableExtra(KEY_USER_DETAIL));
        mPresenter = (UserProgressPresenter) super.mPresenter;
        mPresenter.attachView(this);
    }

    private void initView() {
        mCategoryAdapter = new UserProgressAdapter(mContext, Category.CATEGORY_TRAINING);
        mContentListView.setAdapter(mCategoryAdapter);
        mContentListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPresenter.refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPresenter.loadMore();
            }
        });

        mContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onItemClick((Category) parent.getAdapter().getItem(position), position - 1);
            }
        });
    }

    private void initData(UserDetail userDetail) {
        if (userDetail == null) {
            finish();
        }
        String str1 = " <font color=\'#ffffff\'><b>" + getResources().getString(R.string.study_fir) + "</b></font>";// 你的学习进度
        String str2 = " <font color=\'#ffffff\'><b>" + getResources().getString(R.string.study_sec) + "</b></font>";// 的用户
        String html = str1 + " <font color=\'#DD523f\'><b>" + userDetail.getStudyRank() + "%</b></font>" + str2;
        mRankTextView.setText(Html.fromHtml(html));
        if (userDetail.getStudyRank() >= 0 && userDetail.getStudyRank() <= 50) {
            mDescriptionTextView.setText(R.string.study_level_low);
        } else if (userDetail.getStudyRank() > 50 && userDetail.getStudyRank() <= 80) {
            mDescriptionTextView.setText(R.string.study_level_middle);
        } else {
            mDescriptionTextView.setText(R.string.study_level_high);
        }
    }

    @OnClick(R.id.back_image_view)
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.bottom_button)
    void onBottomClicked() {
        mPresenter.toCategoryListPage();
    }

    @Override
    public Presenter getPresenter() {
        return new UserProgressPresenter(mContext, Category.CATEGORY_TRAINING);
    }

    @Override
    public void showNoneResult() {

    }

    @Override
    public void hideNoneResult() {

    }

    @Override
    public void disablePullUp() {
        mContentListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    public void enablePullUp() {
        mContentListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void startRefreshing() {
        mContentListView.setRefreshing();
    }

    @Override
    public boolean isRefreshing() {
        return mContentListView.isRefreshing();
    }

    @Override
    public void refreshComplete() {
        mContentListView.onRefreshComplete();
    }

    @Override
    public void setData(List<Category> data) {
        System.out.println("set data");
        mCategoryAdapter.setList(data);
    }

    @Override
    public void addData(List<Category> data) {
        mCategoryAdapter.addList(data);
    }

    @Override
    public int getDataCount() {
        return mCategoryAdapter.getCount();
    }

    @Override
    public void setItem(int index, Category item) {
        mCategoryAdapter.setItem(index, item);
    }

    @Override
    public Category getItem(int index) {
        return mCategoryAdapter.getItem(index);
    }

    @Override
    public void removeItem(int index) {
        mCategoryAdapter.remove(index);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}
