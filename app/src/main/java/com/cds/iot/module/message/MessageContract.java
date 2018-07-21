package com.cds.iot.module.message;

import com.cds.iot.module.BasePresenter;
import com.cds.iot.module.BaseView;

public interface MessageContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
