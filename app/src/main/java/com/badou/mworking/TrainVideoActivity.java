package com.badou.mworking;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.presenter.CategoryBasePresenter;
import com.badou.mworking.presenter.TrainingMediaPresenter;
import com.badou.mworking.util.Constant;
import com.badou.mworking.util.DensityUtil;
import com.badou.mworking.view.TrainMediaView;
import com.badou.mworking.widget.FullScreenVideoView;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrainVideoActivity extends TrainBaseActivity implements TrainMediaView {

    @Bind(R.id.video_title_text_view)
    TextView mVideoTitleTextView;
    @Bind(R.id.file_size_text_view)
    TextView mFileSizeTextView;
    @Bind(R.id.top_container_layout)
    LinearLayout mTopContainerLayout;
    @Bind(R.id.video_player)
    FullScreenVideoView mVideoPlayer;
    @Bind(R.id.download_image_view)
    ImageView mDownloadImageView;
    @Bind(R.id.downloading_progress_bar)
    ProgressBar mDownloadingProgressBar;
    @Bind(R.id.player_container_layout)
    FrameLayout mPlayerContainerLayout;
    @Bind(R.id.player_control_image_view)
    ImageView mPlayerControlImageView;
    @Bind(R.id.current_time_text_view)
    TextView mCurrentTimeTextView;
    @Bind(R.id.progress_seek_bar)
    SeekBar mProgressSeekBar;
    @Bind(R.id.total_time_text_view)
    TextView mTotalTimeTextView;
    @Bind(R.id.rotation_check_box)
    CheckBox mRotationCheckBox;
    @Bind(R.id.progress_container_layout)
    LinearLayout mProgressContainerLayout;
    @Bind(R.id.container_layout)
    RelativeLayout mContainerLayout;

    private TrainingMediaPresenter mPresenter;
    private int lastTime = 0;

    public static Intent getIntent(Context context, String rid, boolean isTraining) {
        return TrainBaseActivity.getIntent(context, TrainVideoActivity.class, rid, isTraining);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        mRotationCheckBox.setChecked(isVertical());
        initListener();
        if (isVertical()) {
            showVerticalView();
        } else {
            showHorizontalView();
        }
    }

    @Override
    public CategoryBasePresenter getPresenter(Context context, int type) {
        mPresenter = new TrainingMediaPresenter(context, type, Constant.MWKG_FORAMT_TYPE_MPEG);
        mPresenter.attachView(this);
        return mPresenter;
    }

    /**
     * 功能描述: 监听初始化
     */
    private void initListener() {

        mProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPresenter.onProgressChanged(progress, fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @OnClick(R.id.download_image_view)
    void onDownloadClicked() {
        mPresenter.startDownload();
    }

    @OnClick(R.id.player_control_image_view)
    void onControlClicked() {
        mPresenter.statusChange(mVideoPlayer.isPlaying());
    }

    @OnClick(R.id.rotation_check_box)
    void onRotationClicked() {
        if (!isVertical()) {
            mRotationCheckBox.setChecked(true);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mPresenter.onRotationChanged(false);
        } else {
            mRotationCheckBox.setChecked(false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mPresenter.onRotationChanged(true);
        }
    }

    @OnClick(R.id.player_container_layout)
    void onPlayerLayoutClicked() {
        setProgressBar(mProgressContainerLayout.getVisibility() == View.GONE);
    }

    @Override
    public void setData(String rid, CategoryDetail categoryDetail) {
        super.setData(rid, categoryDetail);
        mVideoTitleTextView.setText(categoryDetail.getSubject());
    }

    /**
     * 没下载的状态 *
     */
    public void statusNotDownLoad() {
        mTotalTimeTextView.setText("0%");
        mDownloadImageView.setVisibility(View.VISIBLE);
        mPlayerControlImageView.setVisibility(View.GONE);
        mCurrentTimeTextView.setVisibility(View.GONE);
        mDownloadingProgressBar.setVisibility(View.GONE);
        mRotationCheckBox.setVisibility(View.GONE);
        mProgressSeekBar.setEnabled(false);// 下载中禁止用户拖动
        mProgressSeekBar.setThumb(new ColorDrawable(android.R.color.transparent));
    }

    /**
     * 下载完成,可以播放的状态 *
     */
    @Override
    public void statusDownloadFinish(File file) {
        mDownloadImageView.setVisibility(View.GONE);
        mPlayerControlImageView.setVisibility(View.VISIBLE);
        mCurrentTimeTextView.setVisibility(View.VISIBLE);
        mDownloadingProgressBar.setVisibility(View.GONE);
        mRotationCheckBox.setVisibility(View.VISIBLE);
        mProgressSeekBar.setEnabled(true);
        mProgressSeekBar.setThumb(getResources().getDrawable(R.drawable.seekbar_));
        float fileSize = ((float) file.length()) / 1024f / 1024f;
        setFileSize(fileSize);
        initVideo(file);
    }

    /**
     * 下载中 *
     */
    @Override
    public void statusDownloading() {
        mDownloadImageView.setVisibility(View.GONE);
        mPlayerControlImageView.setVisibility(View.GONE);
        mCurrentTimeTextView.setVisibility(View.GONE);
        mDownloadingProgressBar.setVisibility(View.VISIBLE);
        mRotationCheckBox.setVisibility(View.GONE);
        mProgressSeekBar.setEnabled(false);// 下载中禁止用户拖动
        mProgressSeekBar.setThumb(new ColorDrawable(android.R.color.transparent));
    }

    @Override
    public void setProgress(long bytesWritten, long totalSize) {
        if (mProgressSeekBar.getMax() != (int) totalSize)
            mProgressSeekBar.setMax((int) totalSize);
        mProgressSeekBar.setProgress((int) bytesWritten);
        if (totalSize > 0 && bytesWritten > 0) {
            mTotalTimeTextView.setText(100 * bytesWritten / totalSize + "%");
        }
    }

    /**
     * 功能描述: 竖屏显示布局
     */
    public void showVerticalView() {
        mBottomView.setVisibility(View.VISIBLE);
        mTopContainerLayout.setVisibility(View.VISIBLE);
        getSupportActionBar().show();
        int height = getResources().getDimensionPixelSize(R.dimen.media_play_height);
        int screenWidth = DensityUtil.getWidthInPx(this);
        int marginLR = getResources().getDimensionPixelOffset(R.dimen.offset_lless);
        mContainerLayout.setPadding(marginLR, 0, marginLR, 0);

        mPlayerContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height));

        RelativeLayout.LayoutParams progressLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        progressLayout.addRule(RelativeLayout.BELOW, R.id.player_container_layout);
        mProgressContainerLayout.setLayoutParams(progressLayout);

        mVideoPlayer.setVideoWidth(screenWidth - 2 * marginLR);
        mVideoPlayer.setVideoHeight(height);
        mVideoPlayer.invalidate();
    }

    /**
     * 功能描述: 横屏隐藏布局
     */
    public void showHorizontalView() {
        mBottomView.setVisibility(View.GONE);
        mTopContainerLayout.setVisibility(View.GONE);
        getSupportActionBar().hide();
        int screenHeight = DensityUtil.getInstance().getScreenHeight();
        int screenWidth = DensityUtil.getInstance().getScreenWidth();
        mContainerLayout.setPadding(0, 0, 0, 0);

        mPlayerContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight));

        RelativeLayout.LayoutParams progressLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        progressLayout.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.player_container_layout);
        mProgressContainerLayout.setLayoutParams(progressLayout);

        mVideoPlayer.setVideoWidth(screenWidth);
        mVideoPlayer.setVideoHeight(screenHeight);
        mVideoPlayer.invalidate();
    }

    @Override
    public void setPlayTime(int time, boolean isVideo) {
        if (isVideo)
            mVideoPlayer.seekTo(time);
        mProgressSeekBar.setProgress(time);
        mCurrentTimeTextView.setText(new SimpleDateFormat("mm:ss").format(time));
    }

    @Override
    public void setFileSize(float fileSize) {
        mFileSizeTextView.setText(String.format("视频文件（%.1fM）", fileSize));
    }

    @Override
    public void setProgressBar(boolean visible) {
        if (!visible && mProgressContainerLayout.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                }
            });

            mProgressContainerLayout.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mProgressContainerLayout.setVisibility(View.GONE);
                }
            });
            mProgressContainerLayout.startAnimation(animation1);
        } else if (visible && mProgressContainerLayout.getVisibility() == View.GONE) {
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_top);
            mProgressContainerLayout.setVisibility(View.VISIBLE);
            mProgressContainerLayout.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_bottom);
            mProgressContainerLayout.startAnimation(animation1);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mPresenter.onRotationChanged(isVertical()); // 竖屏
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 功能描述:初始化播放器
     */
    private void initVideo(File file) {
        mCurrentTimeTextView.setText("00:00");
        mProgressSeekBar.setProgress(0);
        try {
            mVideoPlayer.setVideoURI(Uri.fromFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            mPresenter.fileCrashed();
            return;
        }
        mVideoPlayer.requestFocus();
        mVideoPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 设置大小
                mVideoPlayer.setVideoWidth(mp.getVideoWidth());
                mVideoPlayer.setVideoHeight(mp.getVideoHeight());
                mProgressSeekBar.setMax(mVideoPlayer.getDuration());
                mTotalTimeTextView.setText(new SimpleDateFormat("mm:ss").format(mVideoPlayer.getDuration()));
                setPlayTime(lastTime, true);
            }
        });

        mVideoPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerControlImageView.setImageResource(R.drawable.button_media_start);
                mCurrentTimeTextView.setText("00:00");
                mProgressSeekBar.setProgress(0);
            }
        });
    }

    /**
     * 功能描述: 视屏开始播放
     */
    public void startPlay() {
        if (mVideoPlayer != null && !mVideoPlayer.isPlaying()) {
            mVideoPlayer.start();
            mVideoPlayer.setBackgroundColor(0x00000000);
            mPlayerControlImageView.setImageResource(R.drawable.button_media_stop);
        }
    }

    @Override
    public void stopPlay() {
        if (mVideoPlayer != null && mVideoPlayer.isPlaying()) {
            mVideoPlayer.pause();
            mPlayerControlImageView.setImageResource(R.drawable.button_media_start);
        }
    }

    @Override
    public int getCurrentTime() {
        return mVideoPlayer.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        return mVideoPlayer.isPlaying();
    }

    @Override
    public boolean isVertical() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private class AnimationImp implements AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }

    @Override
    protected void onPause() {
        mPresenter.pause();
        super.onPause();
    }

    // 来电处理
    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state.containsKey("position")) {
            lastTime = state.getInt("position");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", getCurrentTime());
    }
}
