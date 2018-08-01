package com.cds.iot.module.register;

import com.cds.iot.module.BasePresenter;
import com.cds.iot.module.BaseView;

public interface RegisterContact {
    interface View extends BaseView<Presenter> {
        void registerSuccess();
    }

    interface Presenter extends BasePresenter {
        void register(String phone, String password, String code);
    }
}
