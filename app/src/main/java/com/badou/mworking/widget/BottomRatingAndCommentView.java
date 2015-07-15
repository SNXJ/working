package com.badou.mworking.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badou.mworking.CommentActivity;
import com.badou.mworking.R;
import com.badou.mworking.base.BaseActionBarActivity;
import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.net.Net;
import com.badou.mworking.net.ServiceProvider;
import com.badou.mworking.net.volley.VolleyListener;

import org.json.JSONObject;

public class BottomRatingAndCommentView extends LinearLayout {

    public static final int REQUEST_COMMENT = 145;

    private Context mContext;
    private String mRid;
    private int mCurrentScore;
    private LinearLayout mRatingLayout;
    private LinearLayout mCommentLayout;
    private TextView mRatingNumberTextView;
    private TextView mCommentNumberTextView;
    private View mDividerView;

    public BottomRatingAndCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_bottom_comment_and_rating, this);
        initView();
        initListener();
        initAttr(context, attrs);
    }

    public void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomRatingAndCommentView);
            boolean showRating = typedArray.getBoolean(R.styleable.BottomRatingAndCommentView_showRating, true);
            boolean showComment = typedArray.getBoolean(R.styleable.BottomRatingAndCommentView_showComment, true);
            typedArray.recycle();
            setContent(showRating, showComment);
        }
    }

    public void setContent(boolean isRating, boolean isComment) {
        if (isRating) {
            mRatingLayout.setVisibility(VISIBLE);
        } else {
            mRatingLayout.setVisibility(GONE);
            mDividerView.setVisibility(GONE);
        }
        if (isComment) {
            mCommentLayout.setVisibility(VISIBLE);
        } else {
            mCommentLayout.setVisibility(GONE);
            mDividerView.setVisibility(GONE);
        }
    }

    private void initView() {
        mRatingLayout = (LinearLayout) findViewById(R.id.ll_bottom_rating);
        mCommentLayout = (LinearLayout) findViewById(R.id.ll_bottom_comment);
        mRatingNumberTextView = (TextView) findViewById(R.id.tv_bottom_rating_number);
        mCommentNumberTextView = (TextView) findViewById(R.id.tv_bottom_comment_number);
        mDividerView = findViewById(R.id.view_bottom_center_divider);
    }

    private void initListener() {
        mRatingLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new RatingDilog(mContext, mRid, mCurrentScore, new RatingDilog.OnRatingCompletedListener() {

                    @Override
                    public void onRatingCompleted(int score) {
                        mCurrentScore = score;
                        updateData();
                    }
                }).show();
            }
        });
        mCommentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra(CommentActivity.KEY_RID, mRid);
                intent.putExtra(BaseActionBarActivity.KEY_TITLE, mContext.getResources().getString(R.string.title_name_comment));
                ((Activity) mContext).startActivityForResult(intent, REQUEST_COMMENT);
            }
        });
    }

    public void setData(String rid, int ratingNumber, int commentNumber, int currentRating) {
        this.mRid = rid;
        setData(ratingNumber, commentNumber, currentRating);
        // updateData();
    }

    public void setData(int ratingNumber, int commentNumber, int currentRating) {
        setRatingData(ratingNumber, currentRating);
        setCommentData(commentNumber);
    }

    public void setRatingData(int ratingNumber, int currentRating) {
        this.mCurrentScore = currentRating;
        mRatingNumberTextView.setText(String.format("(%d)", ratingNumber));
    }

    public void setCommentData(int commentNumber) {
        mCommentNumberTextView.setText(String.format("(%d)", commentNumber));
    }

    public void updateData() {
        if (TextUtils.isEmpty(mRid))
            return;
        ServiceProvider.getResourceDetail(mContext, mRid, new VolleyListener(mContext) {

            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                CategoryDetail categoryDetail = new CategoryDetail(mContext, jsonObject.optJSONObject(Net.DATA));
                setData(categoryDetail.ratingNum, categoryDetail.commentNum, categoryDetail.rating);
                if (onRatingCommentDataUpdated != null) {
                    onRatingCommentDataUpdated.onDataChanged(categoryDetail.ratingNum, categoryDetail.commentNum, categoryDetail.rating);
                }
            }
        });
    }

    public interface OnRatingCommentDataUpdated {
        void onDataChanged(int ratingNumber, int commentNumber, int currentRating);
    }

    OnRatingCommentDataUpdated onRatingCommentDataUpdated;

    public void setOnRatingCommentDataUpdated(OnRatingCommentDataUpdated onRatingCommentDataUpdated) {
        this.onRatingCommentDataUpdated = onRatingCommentDataUpdated;
    }
}
