package com.cds.iot.module.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;
import com.cds.iot.module.forget.ForgetActivity;
import com.cds.iot.module.login.LoginActivity;
import com.cds.iot.module.setting.notify.MessageNotifyActivity;
import com.cds.iot.module.setting.update.UpdateActivity;
import com.cds.iot.util.AppManager;
import com.cds.iot.util.AppUtils;
import com.cds.iot.util.DeviceUtils;
import com.cds.iot.view.CustomDialog;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    TextView versionNameTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.logout_layout).setOnClickListener(this);
        findViewById(R.id.modify_password_layout).setOnClickListener(this);
        findViewById(R.id.update_layout).setOnClickListener(this);
        findViewById(R.id.message_notify_layout).setOnClickListener(this);
        findViewById(R.id.clean_cache_layout).setOnClickListener(this);
        versionNameTv = (TextView) findViewById(R.id.version_name_tv);
    }

    @Override
    protected void initData() {
        versionNameTv.setText(AppUtils.getVersionName(this));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        CustomDialog customDialog;
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.logout_layout:
                customDialog = new CustomDialog(this)
                        .setMessage("确认退出该账号？")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent().setClass(SettingActivity.this, LoginActivity.class);
                                startActivity(i);
                                AppManager.getInstance().finishAllActivity();
                            }
                        })
                        .setCancelButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).showDialog();
                break;
            case R.id.modify_password_layout:
                intent = new Intent().setClass(this, ForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.update_layout:
                intent = new Intent().setClass(this, UpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.message_notify_layout:
                intent = new Intent().setClass(this, MessageNotifyActivity.class);
                startActivity(intent);
                break;
            case R.id.clean_cache_layout:
                customDialog = new CustomDialog(this)
                        .setMessage("确定要清空缓存？")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setCancelButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).showDialog();
                break;
        }
    }
}
