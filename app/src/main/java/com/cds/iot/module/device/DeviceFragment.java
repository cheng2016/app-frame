package com.cds.iot.module.device;

import android.os.Bundle;
import android.view.View;

import com.cds.iot.R;
import com.cds.iot.base.BaseFragment;

public class DeviceFragment extends BaseFragment implements DeviceContract.View{

    public static DeviceFragment newInstance() {
        DeviceFragment mainFragment = new DeviceFragment();
        new DevicePresenter(mainFragment);
        return mainFragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(DeviceContract.Presenter presenter) {

    }
}
