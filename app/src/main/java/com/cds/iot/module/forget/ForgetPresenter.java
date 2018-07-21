package com.cds.iot.module.forget;

import com.cds.iot.data.source.remote.HttpApi;
import com.cds.iot.data.source.remote.HttpFactory;

import io.reactivex.disposables.CompositeDisposable;

public class ForgetPresenter implements ForgetContract.Presenter{
    public final static String TAG = "ForgetPresenter";
    private ForgetContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public ForgetPresenter(ForgetContract.View view) {
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
