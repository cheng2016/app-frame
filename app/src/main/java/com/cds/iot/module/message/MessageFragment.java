package com.cds.iot.module.message;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cds.iot.R;
import com.cds.iot.base.BaseFragment;
import com.cds.iot.view.ConfirmPopWindow;

public class MessageFragment extends BaseFragment implements MessageContract.View, View.OnClickListener {

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
        view.findViewById(R.id.right_button).setVisibility(View.VISIBLE);
        view.findViewById(R.id.right_button).setOnClickListener(this);
        ((TextView)view.findViewById(R.id.title)).setText("消息");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.right_button:
                new ConfirmPopWindow(getActivity()).showAtBottom(view);
                break;
        }
    }
}
