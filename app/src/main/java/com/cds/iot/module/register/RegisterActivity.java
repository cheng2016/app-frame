package com.cds.iot.module.register;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;

public class RegisterActivity extends BaseActivity implements RegisterContact.View, View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        ((TextView) findViewById(R.id.title)).setText("注册");
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
    public void registerSuccess() {

    }

    @Override
    public void setPresenter(RegisterContact.Presenter presenter) {

    }
}
