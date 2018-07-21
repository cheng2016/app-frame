package com.cds.iot.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cds.iot.R;
import com.cds.iot.base.BaseFragment;
import com.cds.iot.module.about.AboutActivity;
import com.cds.iot.module.description.ProductDescritptionActivity;
import com.cds.iot.module.feedback.FeedBackActivity;
import com.cds.iot.module.setting.SettingActivity;
import com.cds.iot.module.user.detail.UserDetailActivity;
import com.cds.iot.module.wx.WxPublicNumblerActivity;

public class UserFragment extends BaseFragment implements UserContract.View, View.OnClickListener {
    public final static String TAG = "UserFragment";

    public static UserFragment newInstance() {
        UserFragment mainFragment = new UserFragment();
        new UserPresenter(mainFragment);
        return mainFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.setting_layout).setOnClickListener(this);
        view.findViewById(R.id.product_description_layout).setOnClickListener(this);
        view.findViewById(R.id.about_us_layout).setOnClickListener(this);
        view.findViewById(R.id.feedback_layout).setOnClickListener(this);
        view.findViewById(R.id.wx_public_number_layout).setOnClickListener(this);
        view.findViewById(R.id.user_detail_layout).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.user_detail_layout:
                intent = new Intent().setClass(getActivity(), UserDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.product_description_layout:
                intent = new Intent().setClass(getActivity(), ProductDescritptionActivity.class);
                startActivity(intent);
                break;
            case R.id.about_us_layout:
                intent = new Intent().setClass(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback_layout:
                intent = new Intent().setClass(getActivity(), FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.wx_public_number_layout:
                intent = new Intent().setClass(getActivity(), WxPublicNumblerActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_layout:
                intent = new Intent().setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
