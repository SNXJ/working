package com.badou.mworking.widget;

import android.content.Context;

import com.badou.mworking.R;

import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.widget.ProgressBar;

/**
 * Created by Administrator on 2015/5/19.
 *
 * 功能描述:初始化AlertDialog的布局 初始化progerssBar
 *
 */
public class HorizontalProgressDialog extends Dialog{

    ProgressBar progressBar;

    public HorizontalProgressDialog(Context context) {
        super(context);
        // 对话框加载布局文件
        setContentView(R.layout.load_progerss_layout);
        progressBar = (ProgressBar) findViewById(R.id.load_progressBar);
        progressBar.setProgress(0);
    }

    public void setProgress(int progress){
        progressBar.setProgress(progress);
    }

    public void setProgressMax(int max){
        progressBar.setMax(max);
    }
}