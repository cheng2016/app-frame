package com.cds.iot.module.message;

import com.cds.iot.data.source.remote.HttpApi;
import com.cds.iot.data.source.remote.HttpFactory;

import io.reactivex.disposables.CompositeDisposable;

public class MessagePresenter implements MessageContract.Presenter{
    public final static String TAG = "MessagePresenter";
    private MessageContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public MessagePresenter(MessageContract.View view) {
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
