package com.cds.iot.module.register;

import com.cds.iot.data.source.remote.HttpApi;
import com.cds.iot.data.source.remote.HttpFactory;

import io.reactivex.disposables.CompositeDisposable;

public class RegisterPresenter implements RegisterContact.Presenter {
    public final static String TAG = "RegisterPresenter";
    private RegisterContact.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public RegisterPresenter(RegisterContact.View view) {
        this.view = view;
        view.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        mHttpApi =  HttpFactory.createRetrofit2(HttpApi.class);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
