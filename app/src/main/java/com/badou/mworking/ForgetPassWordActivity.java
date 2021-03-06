package com.badou.mworking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badou.mworking.base.BaseBackActionBarActivity;
import com.badou.mworking.util.ToastUtil;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.ChatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseBackActionBarActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, ForgetPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        setActionbarTitle(R.string.title_name_forget_password);
    }

    @OnClick(R.id.phone_layout)
    void toPhoneActivity() {
        startActivity(new Intent(this, ForgetPasswordPhoneActivity.class));
    }

    @OnClick(R.id.service_layout)
    void toServiceActivity() {
        mProgressDialog.show();
        ((DemoHXSDKHelper) DemoHXSDKHelper.getInstance()).loginAnonymous(mActivity, new EMCallBack() {
            @Override
            public void onSuccess() {
                mProgressDialog.dismiss();
                startActivity(ChatActivity.getServiceIntent(mContext));
            }

            @Override
            public void onError(int i, String s) {
                mProgressDialog.dismiss();
                ToastUtil.showToast(mContext, R.string.Login_failed);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick(R.id.call_text_view)
    void call() {
        Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + "4008233773"));
        startActivity(phoneIntent);
    }
}