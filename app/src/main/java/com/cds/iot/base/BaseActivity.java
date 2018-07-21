package com.cds.iot.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cds.iot.module.service.BindService;
import com.cds.iot.util.AppManager;
import com.cds.iot.util.Logger;

import butterknife.ButterKnife;

/**
 * Created by chengzj on 2017/6/17.
 */

public abstract class BaseActivity extends AppCompatActivity{
    public String TAG = "";
    protected BindService mGPSService;

    protected ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BindService.MyBinder binder = (BindService.MyBinder) service;
            mGPSService = binder.getService();
            Logger.v(TAG,"onServiceConnected");
        }
        //client 和service连接意外丢失时，会调用该方法
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.v(TAG,"onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TAG = this.getClass().getSimpleName();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        //绑定服务
        Intent intent = new Intent(this, BindService.class);
        intent.putExtra("from", TAG);
        bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
        initView(savedInstanceState);
        initData();
        AppManager.getInstance().addActivity(this);
    }

    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(mServiceConnection);
        AppManager.getInstance().finishActivity(this);
    }
}
