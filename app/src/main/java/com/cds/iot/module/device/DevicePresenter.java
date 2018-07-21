package com.cds.iot.module.device;

import com.cds.iot.data.source.remote.HttpApi;
import com.cds.iot.data.source.remote.HttpFactory;

import io.reactivex.disposables.CompositeDisposable;

public class DevicePresenter implements DeviceContract.Presenter{
    public final static String TAG = "DevicePresenter";
    private DeviceContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public DevicePresenter(DeviceContract.View view) {
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
