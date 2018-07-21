package com.cds.iot.module.device;

import com.cds.iot.module.BasePresenter;
import com.cds.iot.module.BaseView;

public interface DeviceContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
