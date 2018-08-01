package com.cds.iot.module.login;

import com.cds.iot.App;
import com.cds.iot.data.entity.BaseReq;
import com.cds.iot.data.entity.BaseResp;
import com.cds.iot.data.entity.LoginReq;
import com.cds.iot.data.source.remote.BaseObserver;
import com.cds.iot.data.source.remote.HttpApi;
import com.cds.iot.data.source.remote.HttpFactory;
import com.cds.iot.util.DeviceUtils;
import com.cds.iot.util.MD5Utils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {
    public final static String TAG = "LoginPresenter";
    private LoginContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        view.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        mHttpApi = HttpFactory.createRetrofit2(HttpApi.class);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void login(String name, String pwd) {
        LoginReq loginReq = new LoginReq(name, MD5Utils.MD5(MD5Utils.MD5(pwd)));
        BaseReq req = new BaseReq(loginReq);
        mHttpApi.login(new Gson().toJson(req), MD5Utils.MD5(name + pwd + DeviceUtils.getDeviceIMEI(App.getInstance())))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        view.loginSuccess();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
