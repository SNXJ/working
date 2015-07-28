package com.badou.mworking.presenter;

import android.content.Context;
import android.content.Intent;

import com.badou.mworking.R;
import com.badou.mworking.domain.ChatterReplyGetUseCase;
import com.badou.mworking.domain.ChatterReplySendUseCase;
import com.badou.mworking.domain.ChatterUseCase;
import com.badou.mworking.domain.UseCase;
import com.badou.mworking.entity.chatter.Chatter;
import com.badou.mworking.entity.comment.ChatterComment;
import com.badou.mworking.net.BaseNetEntity;
import com.badou.mworking.net.BaseSubscriber;
import com.badou.mworking.util.GsonUtil;
import com.badou.mworking.view.BaseView;
import com.badou.mworking.view.ChatterDetailView;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ChatterDetailPresenter extends CommentPresenter {

    String mQid;
    Chatter mChatter;
    ChatterReplyGetUseCase mReplyGetUseCase;
    ChatterReplySendUseCase mReplySendUseCase;
    ChatterDetailView mDetailView;

    public ChatterDetailPresenter(Context context, String qid) {
        super(context);
        this.mQid = qid;
    }

    @Override
    public void attachView(BaseView v) {
        mDetailView = (ChatterDetailView) v;
        super.attachView(v);
    }

    @Override
    protected void initialize() {
        mDetailView.showProgressDialog();
        new ChatterUseCase(mQid).execute(new BaseSubscriber<Chatter>(mContext) {
            @Override
            public void onResponseSuccess(Chatter data) {
                mChatter = data;
                mDetailView.setData(data);
                // 先获取详情，再刷新列表
                ChatterDetailPresenter.super.initialize();
            }

            @Override
            public void onCompleted() {
                mDetailView.hideProgressDialog();
            }
        });
    }

    @Override
    public void submitComment(String comment) {
        mDetailView.showProgressDialog(R.string.action_comment_update_ing);
        if (mReplySendUseCase == null)
            mReplySendUseCase = new ChatterReplySendUseCase(mQid);
        mReplySendUseCase.setData(comment, mWhom);
        mReplySendUseCase.execute(new BaseSubscriber<BaseNetEntity>(mContext) {
            @Override
            public void onResponseSuccess(BaseNetEntity data) {
                mDetailView.startRefreshing();
            }

            @Override
            public void onCompleted() {
                mDetailView.hideProgressDialog();
            }
        });
    }

    @Override
    protected Type getType() {
        return new TypeToken<List<ChatterComment>>() {
        }.getType();
    }

    @Override
    protected UseCase getRefreshUseCase(int pageIndex) {
        if (mReplyGetUseCase == null)
            mReplyGetUseCase = new ChatterReplyGetUseCase(mQid);
        mReplyGetUseCase.setPageNum(pageIndex);
        return mReplyGetUseCase;
    }

    public Intent getResult(){
        mChatter.setReplyNumber(mDetailView.getDataCount());
        return ListPresenter.getResultIntent(GsonUtil.toJson(mChatter, Chatter.class));
    }
}
