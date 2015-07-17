package com.badou.mworking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.badou.mworking.adapter.CategoryAdapterFactory;
import com.badou.mworking.adapter.ClassificationAdapter;
import com.badou.mworking.base.BaseBackActionBarActivity;
import com.badou.mworking.base.MyBaseAdapter;
import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.category.Classification;
import com.badou.mworking.presenter.CategoryListPresenter;
import com.badou.mworking.presenter.TrainingListPresenter;
import com.badou.mworking.util.DensityUtil;
import com.badou.mworking.view.CategoryListView;
import com.badou.mworking.widget.NoneResultView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnTouch;

public class CategoryListActivity extends BaseBackActionBarActivity implements CategoryListView {

    public static final String KEY_CATEGORY = "category";

    @Bind(R.id.content_list_view)
    PullToRefreshListView mContentListView;
    @Bind(R.id.none_result_view)
    NoneResultView mNoneResultView;
    @Bind(R.id.classification_main_list)
    ListView mClassificationMainList;
    @Bind(R.id.classification_more_list)
    ListView mClassificationMoreList;
    @Bind(R.id.classification_container)
    LinearLayout mClassificationContainer;
    @Bind(R.id.classification_background)
    FrameLayout mClassificationBackground;

    private ImageView mTitleTriangleImageView;
    private View mTitleLayout;
    private TextView mTitleReadTextView;

    private ClassificationAdapter mMainClassificationAdapter = null;
    private ClassificationAdapter mMoreClassificationAdapter = null;
    protected MyBaseAdapter<Category> mCategoryAdapter = null;
    private int mCategoryIndex;

    CategoryListPresenter mCategoryPresenter;

    public static Intent getIntent(Context context, int category) {
        Intent intent = new Intent(context, CategoryListActivity.class);
        intent.putExtra(KEY_CATEGORY, category);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_progress_list);
        ButterKnife.bind(this);
        mCategoryIndex = mReceivedIntent.getIntExtra(KEY_CATEGORY, -1);
        if (mCategoryIndex == -1) {
            finish();
            return;
        }
        switch (mCategoryIndex) {
            case Category.CATEGORY_TRAINING:
                mCategoryPresenter = new TrainingListPresenter(mContext, mCategoryIndex);
                break;
            default:
                mCategoryPresenter = new CategoryListPresenter(mContext, mCategoryIndex);
        }
        initTitleView();
        initClassificationView();
        initListView();
        mCategoryPresenter.attachView(this);
    }

    /**
     * 初始化action 布局
     */
    private void initTitleView() {
        mTitleReadTextView = new TextView(mContext);
        mTitleReadTextView.setText(R.string.category_unread);
        mTitleReadTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.getInstance().getTextSizeSmall());
        int paddingLess = DensityUtil.getInstance().getOffsetLess();
        mTitleReadTextView.setPadding(paddingLess, 0, paddingLess, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, paddingLess, 0);
        mTitleReadTextView.setLayoutParams(layoutParams);
        addTitleRightView(mTitleReadTextView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoryPresenter.onUnreadClick();
            }
        });
        setUnread(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTitleLayout = inflater.inflate(R.layout.actionbar_progress, null);
        setTitleCustomView(mTitleLayout);
        mTitleTriangleImageView = (ImageView) mTitleLayout.findViewById(R.id.iv_actionbar_triangle);
        mTitleTriangleImageView.setVisibility(View.VISIBLE);
        mTitleTextView = (TextView) mTitleLayout.findViewById(R.id.tv_actionbar_title);
        mTitleTextView.setText(mReceivedIntent.getStringExtra(KEY_TITLE));
        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoryPresenter.onClassificationStatusChanged();
            }
        });
    }

    private void initClassificationView() {
        mMainClassificationAdapter = new ClassificationAdapter(mContext, true);
        mMoreClassificationAdapter = new ClassificationAdapter(mContext, false);
        mClassificationMoreList.setAdapter(mMoreClassificationAdapter);
        mClassificationMainList.setAdapter(mMainClassificationAdapter);
    }

    private void initListView() {
        mCategoryAdapter = CategoryAdapterFactory.getAdapter(mContext, mCategoryIndex);
        mContentListView.setAdapter(mCategoryAdapter);
        mContentListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                mCategoryPresenter.refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                mCategoryPresenter.loadMore();
            }
        });
        mContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                Category category = mCategoryAdapter.getItem(position);
                mCategoryPresenter.onItemClick(category, position);
            }
        });
    }

    @OnTouch(R.id.classification_background)
    boolean onClassificationBackgroundTouched(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            mCategoryPresenter.onClassificationStatusChanged();
        return true;
    }

    @OnItemClick(R.id.classification_main_list)
    void onMainClassificationClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        mMainClassificationAdapter.setSelectedPosition(arg2);
        mCategoryPresenter.onClassificationMainClicked(mMainClassificationAdapter.getItem(arg2));
    }

    @OnItemClick(R.id.classification_more_list)
    void onMoreClassificationClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        mMoreClassificationAdapter.setSelectedPosition(arg2);
        mCategoryPresenter.onClassificationMoreClicked(mMoreClassificationAdapter.getItem(arg2));
    }

    @Override
    public void setMainClassification(List<Classification> data) {
        mMainClassificationAdapter.setSelectedPosition(0);
        mClassificationMoreList.setVisibility(View.GONE);
        mMainClassificationAdapter.setList(data);
    }

    @Override
    public void setMoreClassification(List<Classification> data) {
        mMainClassificationAdapter.setSelectedPosition(0);
        mClassificationMoreList.setVisibility(View.VISIBLE);
        mMoreClassificationAdapter.setList(data);
    }

    @Override
    public void showNoneResult() {
        mNoneResultView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoneResult() {
        mNoneResultView.setVisibility(View.INVISIBLE);
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
        hideProgressBar();
        mContentListView.onRefreshComplete();
    }

    @Override
    public void setData(List<Category> data) {
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
    public void removeItem(int index) {
        mCategoryAdapter.remove(index);
    }

    public void showMenu() {
        mTitleTriangleImageView.setImageResource(R.drawable.icon_triangle_up);
        mClassificationBackground.setVisibility(View.VISIBLE);
        mClassificationContainer.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.popup_enter);
        mClassificationContainer.startAnimation(anim);
    }

    public void hideMenu() {
        mTitleTriangleImageView.setImageResource(R.drawable.icon_triangle_down);
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.popup_exit);
        mClassificationContainer.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mClassificationBackground.setVisibility(View.INVISIBLE);
                mClassificationContainer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setUnread(boolean unread) {
        if (unread) {
            mTitleReadTextView.setTextColor(getResources().getColor(R.color.color_white));
            mTitleReadTextView.setBackgroundResource(R.drawable.background_button_enable_blue_normal);
        } else {
            mTitleReadTextView.setTextColor(getResources().getColor(R.color.color_border_grey));
            mTitleReadTextView.setBackgroundResource(R.drawable.background_border_radius_small_grey);
        }
    }

}