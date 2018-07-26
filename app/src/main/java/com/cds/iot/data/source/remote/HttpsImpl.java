package com.cds.iot.data.source.remote;

import android.content.Context;

import com.cds.iot.data.entity.GetWXTokenResp;
import com.cds.iot.data.entity.GetWxUserInfoResp;
import com.cds.iot.util.Logger;
import com.cds.iot.wxapi.WXEntryActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class HttpsImpl {
    public static final String TAG = "HttpsImpl";

    private static volatile Https mSSHClient;

    private HttpsImpl() {
    }

    private Https getSSHClient(Context context) {
        if (mSSHClient == null) {
            synchronized (this) {
                mSSHClient = HttpFactory.createSSLService(Https.class, context);
            }
        }
        return mSSHClient;
    }

    public void getWXUserInfo(final Context context, String code) {
        getSSHClient(context).getWXToken(WXEntryActivity.WX_APP_ID, WXEntryActivity.WX_APP_SECRET, code, "authorization_code")
                .flatMap(new Function<GetWXTokenResp, ObservableSource<GetWxUserInfoResp>>() {
                    @Override
                    public ObservableSource<GetWxUserInfoResp> apply(GetWXTokenResp resp) throws Exception {
                        return getSSHClient(context).getWxUserInfo(resp.getAccess_token(), resp.getOpenid());
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<GetWxUserInfoResp>() {
                    @Override
                    public void onError(Throwable e) {
                        Logger.v(TAG, "getWXUserInfo onError  msg：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetWxUserInfoResp resp) {
                        Logger.v(TAG, "getWXUserInfo onNext  msg：" + new Gson().toJson(resp));
                        EventBus.getDefault().post(resp);
                    }
                });

    }
}
