package com.badou.mworking.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.badou.mworking.BackWebActivity;
import com.badou.mworking.CommentActivity;
import com.badou.mworking.R;
import com.badou.mworking.domain.CategoryDetailUseCase;
import com.badou.mworking.domain.StoreUseCase;
import com.badou.mworking.entity.Store;
import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.entity.user.UserInfo;
import com.badou.mworking.net.BaseNetEntity;
import com.badou.mworking.net.BaseSubscriber;
import com.badou.mworking.net.Net;
import com.badou.mworking.net.ServiceProvider;
import com.badou.mworking.net.volley.VolleyListener;
import com.badou.mworking.view.BaseView;
import com.badou.mworking.view.CategoryBaseView;

import org.json.JSONObject;

public class CategoryBasePresenter extends Presenter {

    public static final String KEY_RID = "rid";
    public static final int REQUEST_COMMENT = 126;

    CategoryBaseView mCategoryBaseView;
    String mRid;
    CategoryDetail mCategoryDetail;
    StoreUseCase mStoreUseCase;

    public CategoryBasePresenter(Context context) {
        super(context);
    }

    @Override
    public void attachView(BaseView v) {
        mCategoryBaseView = (CategoryBaseView) v;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {
        mRid = intent.getStringExtra(KEY_RID);
        new CategoryDetailUseCase(mRid).execute(new BaseSubscriber<CategoryDetail>(mContext) {
            @Override
            public void onResponseSuccess(CategoryDetail data) {
                mCategoryBaseView.setData(data);
                mCategoryDetail = data;
            }

            @Override
            public void onErrorCode(int code) {
                mCategoryBaseView.showToast(R.string.tip_message_center_resource_gone);
                ((Activity) mContext).finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_COMMENT && resultCode == Activity.RESULT_OK && data != null) {
            int commentNumber = data.getIntExtra(CommentActivity.RESPONSE_COUNT, 0);
            mCategoryBaseView.setCommentNumber(commentNumber);
        }
    }

    public void onStatisticalClicked() {
        String titleStr = mContext.getResources().getString(R.string.statistical_data);
        String uid = UserInfo.getUserInfo().getUid();
        String url = Net.getRunHost() + Net.getTongji(uid, mRid);
        Intent intent = new Intent(mContext, BackWebActivity.class);
        intent.putExtra(BackWebActivity.KEY_URL, url);
        intent.putExtra(BackWebActivity.KEY_TITLE, titleStr);
        mContext.startActivity(intent);
    }

    public void onStoreClicked() {
        if (mCategoryDetail.isStore()) {
            mCategoryBaseView.showProgressDialog(R.string.progress_tips_delete_store_ing);
        } else {
            mCategoryBaseView.showProgressDialog(R.string.progress_tips_store_ing);
        }
        if (mStoreUseCase == null)
            mStoreUseCase = new StoreUseCase(mRid, Store.getStoreStringFromCategory(mCategoryDetail.getFmt()));
        mStoreUseCase.setIsAdd(!mCategoryDetail.isStore());
        mStoreUseCase.execute(new BaseSubscriber<BaseNetEntity>(mContext) {
            @Override
            public void onResponseSuccess(BaseNetEntity data) {
                boolean isStore = !mCategoryDetail.isStore();
                mCategoryDetail.setStore(isStore);
                mCategoryBaseView.setStore(isStore);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mCategoryBaseView.hideProgressDialog();
            }
        });
    }

    public void onCommentClicked() {
        ((Activity) mContext).startActivityForResult(CommentActivity.getIntent(mContext, mRid), REQUEST_COMMENT);
    }
}
