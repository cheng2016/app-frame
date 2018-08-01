package com.cds.iot.module.login;

import com.cds.iot.module.BasePresenter;
import com.cds.iot.module.BaseView;

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void loginSuccess();
    }

    interface Presenter extends BasePresenter {
        void login(String name,String pwd);
    }
}
