package com.cds.iot.module.forget;

import com.cds.iot.data.entity.BaseReq;
import com.cds.iot.data.entity.BaseResp;
import com.cds.iot.data.entity.RegisterReq;
import com.cds.iot.data.source.remote.BaseObserver;
import com.cds.iot.data.source.remote.HttpApi;
import com.cds.iot.data.source.remote.HttpFactory;
import com.cds.iot.util.MD5Utils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    @Override
    public void resetPassword(String phone, String pwd, String code) {
        RegisterReq registerReq = new RegisterReq(phone, MD5Utils.MD5(MD5Utils.MD5(pwd)), code);
        BaseReq req = new BaseReq(registerReq);
        mHttpApi.resetpwd(new Gson().toJson(req))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResp>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        view.resetPasswordSuccess();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
