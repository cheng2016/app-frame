package com.cds.iot.module.login;

import com.cds.iot.data.source.remote.HttpApi;
import com.cds.iot.data.source.remote.HttpFactory;

import io.reactivex.disposables.CompositeDisposable;

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

    }
}
