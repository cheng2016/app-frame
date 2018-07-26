package com.cds.iot;

import android.os.Environment;

import com.blankj.utilcode.util.Utils;
import com.cds.iot.base.BaseApplication;
import com.cds.iot.util.DeviceUtils;
import com.cds.iot.util.Logger;
import com.cds.iot.util.ToastUtils;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by chengzj on 2017/6/17.
 */

public class App extends BaseApplication{
    public final static String TAG = "App";

    private static App mInstance;

    private String imageCacheDir = "";

    private String appCacheDir = "";

    public static App getInstance(){
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
        initPicasoConfig();
    }

    private void initPicasoConfig(){
        if (DeviceUtils.isSDCardEnable()) {
            appCacheDir = Environment.getExternalStorageDirectory() + "/wecare/v4/";
        } else {
            appCacheDir = getCacheDir().getAbsolutePath() + "/wecare/v4/";
        }
        File directory = new File(appCacheDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        imageCacheDir = appCacheDir + "image/";
         directory = new File(imageCacheDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Logger.i(TAG, "imageCacheDir：" + imageCacheDir);
        Picasso picasso = new Picasso.Builder(this).downloader(
                new OkHttpDownloader(new File(imageCacheDir))).build();
        Picasso.setSingletonInstance(picasso);
    }

    public String getAppCacheDir() {
        File directory = new File(appCacheDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Logger.i(TAG,"getAppCacheDir：" + appCacheDir);
        return appCacheDir;
    }
}
