package com.cds.iot.module.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cds.iot.R;
import com.cds.iot.base.BaseFragment;
import com.cds.iot.module.device.add.AddDeviceActivity;

public class DeviceFragment extends BaseFragment implements DeviceContract.View, View.OnClickListener {


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
        view.findViewById(R.id.add_img).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(DeviceContract.Presenter presenter) {

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add_img:
                intent = new Intent().setClass(getActivity(), AddDeviceActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
