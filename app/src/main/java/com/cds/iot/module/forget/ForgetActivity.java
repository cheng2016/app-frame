package com.cds.iot.module.forget;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;

public class ForgetActivity extends BaseActivity implements ForgetContract.View,View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        ((TextView)findViewById(R.id.title)).setText("密码重置");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

    @Override
    public void resetPasswordSuccess() {

    }

    @Override
    public void setPresenter(ForgetContract.Presenter presenter) {

    }
}
