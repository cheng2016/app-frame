package com.cds.iot.module.setting.notify;

import android.os.Bundle;
import android.view.View;

import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;

public class MessageNotifyActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_notify;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }
}
