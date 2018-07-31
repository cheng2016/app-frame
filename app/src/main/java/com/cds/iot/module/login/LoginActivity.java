package com.cds.iot.module.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;
import com.cds.iot.module.forget.ForgetActivity;
import com.cds.iot.module.main.MainActivity;
import com.cds.iot.module.register.RegisterActivity;

import butterknife.Bind;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.email)
    AutoCompleteTextView emailView;
    @Bind(R.id.password)
    EditText passwordView;

    String email;
    String password;
//    RxDialogLoading rxDialogLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.register_btn).setOnClickListener(this);
        findViewById(R.id.modify_password_button).setOnClickListener(this);
        findViewById(R.id.logo_img).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                email = emailView.getText().toString().trim();
                password = passwordView.getText().toString().trim();
                if (TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(password)) {
                    ToastUtils.showShort("用户名和密码不能为空！");
                } else {
                    if (!RegexUtils.isEmail(email)) {
                        ToastUtils.showShort("请输入正确的邮箱！");
                    } else {
                        login();
                    }
                }
                break;
            case R.id.logo_img:
                login();
                break;
            case R.id.register_btn:
                Intent intent = new Intent();
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_password_button:
                Intent i = new Intent();
                i.setClass(this, ForgetActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    private void login() {
        /*rxDialogLoading = new RxDialogLoading(LoginActivity.this);
        rxDialogLoading.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rxDialogLoading.cancel();
                startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);*/
        final ProgressDialog  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
