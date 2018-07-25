package com.cds.iot.module.wxbind;

import com.cds.iot.module.BasePresenter;
import com.cds.iot.module.BaseView;

public interface BindContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
