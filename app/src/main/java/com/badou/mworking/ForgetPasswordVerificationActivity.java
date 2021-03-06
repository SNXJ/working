package com.badou.mworking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.badou.mworking.base.AppApplication;
import com.badou.mworking.base.BaseBackActionBarActivity;
import com.badou.mworking.domain.ResetPasswordUseCase;
import com.badou.mworking.entity.user.UserInfo;
import com.badou.mworking.net.BaseSubscriber;
import com.badou.mworking.util.ToastUtil;

import java.util.regex.Pattern;

/**
 * 用户验证身份页(修改密码页)
 *
 * @author
 */
public class ForgetPasswordVerificationActivity extends BaseBackActionBarActivity {

    private static final String LOG = "UserVerifyAct_";
    private EditText mPasswordEditText, mConfirmationEditText;
    private TextView mNextTextView;
    public static final String VERIFY_PHONE = "UserVerify_phone";
    public static final String VERIFY_VCODE = "UserVerify_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_verification);
        setActionbarTitle(getResources().getString(R.string.title_name_ShenFenRenZheng));
        initView();
        initListener();
    }

    protected void initView() {
        mPasswordEditText = (EditText) this.findViewById(R.id.et_forget_password_verification_new);
        mConfirmationEditText = (EditText) this.findViewById(R.id.et_forget_password_verification_confirm);
        mNextTextView = (TextView) this.findViewById(R.id.tv_forget_password_verification_next);
        mProgressDialog.setContent(R.string.progress_tips_submit_ing);
    }

    private void initListener() {
        mNextTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String p1 = mPasswordEditText.getText().toString();
                String p2 = mConfirmationEditText.getText().toString();
                String phone = mReceivedIntent.getStringExtra(VERIFY_PHONE);
                String vcode = mReceivedIntent.getStringExtra(VERIFY_VCODE);
                Pattern pattern = Pattern.compile("^[A-Za-z0-9@\\_\\-\\.]+$");
                boolean a = pattern.matcher(p1).matches();
                boolean b = pattern.matcher(p2).matches();
                if (TextUtils.isEmpty(p1) || TextUtils.isEmpty(p2)) {
                    ToastUtil.showToast(mContext, R.string.login_error_empty_password);
                } else if (!p1.equals(p2)) {
                    ToastUtil.showToast(mContext, R.string.change_error_different_password);
                } else if (p1.length() < 6) {
                    ToastUtil.showToast(mContext, R.string.change_error_short_password_original);
                } else if (p2.length() < 6) {
                    ToastUtil.showToast(mContext, R.string.change_error_short_password_new);
                } else if (!a) {
                    ToastUtil.showToast(mContext, R.string.tips_password_input_invalid);
                } else if (!b) {
                    ToastUtil.showToast(mContext, R.string.tips_password_input_invalid);
                } else if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(vcode)) {
                    ToastUtil.showToast(mContext, R.string.act_expri_verify_err_vcode);
                } else {
                    ChangePass(phone, vcode, p1);
                }
            }
        });
        mPasswordEditText.addTextChangedListener(new PasswordTextWatcher());
        mConfirmationEditText.addTextChangedListener(new PasswordTextWatcher());
    }

    class PasswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String newPassword = mPasswordEditText.getText().toString();
            String confirmPassword = mConfirmationEditText.getText().toString();
            if (!TextUtils.isEmpty(newPassword) && newPassword.length() >= 6 && !TextUtils.isEmpty(confirmPassword) && confirmPassword.length() >= 6) {
                mNextTextView.setTextColor(getResources().getColorStateList(R.color.color_button_text_red));
                mNextTextView.setBackgroundResource(R.drawable.background_button_enable_red);
                mNextTextView.setEnabled(true);
            } else {
                mNextTextView.setTextColor(getResources().getColor(R.color.color_white));
                mNextTextView.setBackgroundResource(R.drawable.background_button_disable);
                mNextTextView.setEnabled(true);
            }
        }
    }

    /**
     * 网络请求 忘记密码
     *
     * @param phoneNum
     * @param vcode
     * @param newpwd
     */
    private void ChangePass(final String phoneNum, String vcode, String newpwd) {
        mProgressDialog.show();
        new ResetPasswordUseCase(phoneNum, vcode, newpwd).execute(new BaseSubscriber<UserInfo>(mContext) {
            @Override
            public void onResponseSuccess(UserInfo data) {
                // 返回码正确时 调用
                ToastUtil.showToast(mContext, R.string.password_success);
                chaSuccess(phoneNum, data);
                finish();
            }

            @Override
            public void onCompleted() {
                mProgressDialog.dismiss();
            }
        });
    }

    /**
     * 登录成功 保存信息
     */
    private void chaSuccess(String acount,UserInfo userInfo) {
        // 保存用户登录成功返回的信息 到sharePreferncers
        UserInfo.setUserInfo((AppApplication) mContext.getApplicationContext(), acount, userInfo);
        goMainGrid();
    }

    /**
     * 功能描述:跳转到主页
     */
    private void goMainGrid() {
        Intent intent = new Intent(mContext, MainGridActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

}
