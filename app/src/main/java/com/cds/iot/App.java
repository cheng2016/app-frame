package com.cds.iot;

import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.cds.iot.base.BaseApplication;
import com.cds.iot.util.Logger;

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
        Logger.initialize(this,true, Logger.Level.VERBOSE);
    }

    void initAppTool(){

        //初始化工具类
        Utils.init(this);
    }
}
