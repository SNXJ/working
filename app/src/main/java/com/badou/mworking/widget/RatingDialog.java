/* 
 * 文件名: CoursewareScoreDilog.java
 * 包路径: com.badou.mworking.widget
 * 创建描述  
 *        创建人：葛建锋
 *        创建日期：2015年1月12日 下午3:06:04
 *        内容描述：
 * 修改描述  
 *        修改人：葛建锋 
 *        修改日期：2015年1月12日 下午3:06:04 
 *        修改内容:
 * 版本: V1.0   
 */
package com.badou.mworking.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.badou.mworking.R;

/**
 * 功能描述: 课件评分Dialog
 */
public class RatingDialog extends Dialog {

    private Context mContext;
    private RatingBar mScoreRatingBar;  // 评分选择器
    private TextView mConfirmTextView; //确定
    private TextView mCancelTextView; //取消
    private TextView mTipsTextView; //得分提醒
    private TextView mTitleTextView; // 文字提醒   轻点星星来评分  您已评过分了

    private LinearLayout mNotRatedLayout;  //评分布局
    private LinearLayout mRatedLayout;//已经评过分了布局

    private int mCurrentScore = 0;

    public OnRatingConfirmListener mOnRatingCompletedListener;

    public interface OnRatingConfirmListener {
        void onRatingConfirm(int rating);
    }

    public RatingDialog(Context context, OnRatingConfirmListener listener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rating);
        this.mOnRatingCompletedListener = listener;
        this.mContext = context;
        initView();
        initListener();
    }

    public void setCurrentScore(int score) {
        this.mCurrentScore = score;
    }

    /**
     * 功能描述: 布局初始化
     */
    private void initView() {
        mScoreRatingBar = (RatingBar) findViewById(R.id.rb_dialog_rating);
        mConfirmTextView = (TextView) findViewById(R.id.btn_dialog_rating_confirm);
        mCancelTextView = (TextView) findViewById(R.id.tv_dialog_rating_cancel);
        mTipsTextView = (TextView) findViewById(R.id.tv_dialog_rating_tips);
        mTitleTextView = (TextView) findViewById(R.id.tv_dialog_rating_title);
        mNotRatedLayout = (LinearLayout) findViewById(R.id.ll_dialog_rating_not_rated);
        mRatedLayout = (LinearLayout) findViewById(R.id.ll_dialog_rating_rated);
        if (mCurrentScore > 0) {
            mNotRatedLayout.setVisibility(View.GONE);
            mRatedLayout.setVisibility(View.VISIBLE);
            mTitleTextView.setText(R.string.dialog_rating_title_rated);
            scoreTips(mCurrentScore);
            mScoreRatingBar.setRating(mCurrentScore);
            // 屏蔽触碰事件，直接使用setEnable(false)的话会使style也失效
            mScoreRatingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
    }

    private void initListener() {
        mConfirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRatingCompletedListener.onRatingConfirm((int) mScoreRatingBar.getRating());
            }
        });
        mCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mRatedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Ratingbar监听器
        mScoreRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int score = (int) rating;
                scoreTips(score);
                if (rating > 0) {
                    mConfirmTextView.setEnabled(true);
                    mConfirmTextView.setBackgroundResource(R.drawable.background_button_enable_blue);
                    mConfirmTextView.setTextColor(mContext.getResources().getColorStateList(R.color.color_button_text_blue));
                } else {
                    mConfirmTextView.setEnabled(false);
                    mConfirmTextView.setBackgroundResource(R.drawable.background_button_disable);
                    mConfirmTextView.setTextColor(mContext.getResources().getColor(R.color.color_white));
                }
            }
        });
    }

    /**
     * 得分文案提醒
     */
    private void scoreTips(int score) {
        switch (score) {
            case 0:
                mTipsTextView.setText("");
                break;
            case 1:
                mTipsTextView.setText(mContext.getResources().getString(R.string.dialog_rating_score_one));
                break;
            case 2:
                mTipsTextView.setText(mContext.getResources().getString(R.string.dialog_rating_score_two));
                break;
            case 3:
                mTipsTextView.setText(mContext.getResources().getString(R.string.dialog_rating_score_three));
                break;
            case 4:
                mTipsTextView.setText(mContext.getResources().getString(R.string.dialog_rating_score_four));
                break;
            case 5:
                mTipsTextView.setText(mContext.getResources().getString(R.string.dialog_rating_score_five));
                break;
            default:
                break;
        }
    }
}
