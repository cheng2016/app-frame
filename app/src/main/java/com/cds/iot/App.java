package com.cds.iot;

import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.cds.iot.base.BaseApplication;
import com.cds.iot.util.Logger;
import com.cds.iot.util.ToastUtils;

/**
 * Created by chengzj on 2017/6/17.
 */

public class App extends BaseApplication{
    private static App mInstance;

    public static Context getInstance(){
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initAppTool();
    }

    void initAppTool(){
        //初始化工具类
        Utils.init(this);
        Logger.initialize(this,true, Logger.Level.VERBOSE);
        ToastUtils.isShow = false;
    }
}
