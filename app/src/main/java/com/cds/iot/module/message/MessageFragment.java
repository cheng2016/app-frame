package com.cds.iot.module.message;

import android.os.Bundle;
import android.view.View;

import com.cds.iot.R;
import com.cds.iot.base.BaseFragment;

public class MessageFragment extends BaseFragment implements MessageContract.View{

    public static MessageFragment newInstance() {
        MessageFragment mainFragment = new MessageFragment();
        new MessagePresenter(mainFragment);
        return mainFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {

    }
}
