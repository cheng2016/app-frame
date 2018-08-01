package com.cds.iot.module.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;
import com.cds.iot.module.forget.ForgetActivity;
import com.cds.iot.module.main.MainActivity;
import com.cds.iot.module.register.RegisterActivity;

import butterknife.Bind;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.acount)
    AppCompatEditText acountView;
    @Bind(R.id.password)
    AppCompatEditText passwordView;
    @Bind(R.id.bg_img)
    ImageView bgImg;

    String acount;
    String password;

//    RxDialogLoading rxDialogLoading;

    AnimationDrawable animationDrawable;

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
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation inalphaAnimation = new AlphaAnimation(0.8f,1);
        inalphaAnimation.setDuration(1200);
        inalphaAnimation.setRepeatCount(Animation.INFINITE);
        inalphaAnimation.setRepeatMode(Animation.REVERSE);

        AlphaAnimation outalphaAnimation = new AlphaAnimation(1,0.7f);
        outalphaAnimation.setDuration(600);
        outalphaAnimation.setRepeatCount(Animation.INFINITE);
        outalphaAnimation.setRepeatMode(Animation.REVERSE);

        animationSet.addAnimation(inalphaAnimation);
        animationSet.addAnimation(outalphaAnimation);

        bgImg.setAnimation(inalphaAnimation);

        bgImg.setImageResource(R.drawable.frame_login_bg_anim);
        animationDrawable = (AnimationDrawable)  bgImg.getDrawable();
        animationDrawable.start();

//        ((Animatable)bgImg.getDrawable()).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                acount = acountView.getText().toString().trim();
                password = passwordView.getText().toString().trim();
                if (TextUtils.isEmpty(acount)
                        || TextUtils.isEmpty(password)) {
                    ToastUtils.showShort("账户和密码不能为空！");
                } else {
                    if (!RegexUtils.isMobileExact(acount)) {
                        ToastUtils.showShort("请输入正确的账号！");
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
        KeyboardUtils.hideSoftInput(this);
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
        final ProgressDialog progressDialog = new ProgressDialog(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationDrawable.stop();
    }
}
