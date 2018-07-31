package com.cds.iot.module.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cds.iot.R;
import com.cds.iot.base.BaseFragment;
import com.cds.iot.module.device.adapter.DeviceAdapter;
import com.cds.iot.module.device.adapter.MenuAdapter;
import com.cds.iot.module.device.add.AddDeviceActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeviceFragment extends BaseFragment implements DeviceContract.View, View.OnClickListener {
    @Bind(R.id.menu_listview)
    ListView menuListview;
    @Bind(R.id.device_gridview)
    GridView deviceGridview;
    @Bind(R.id.device_title)
    TextView deviceTitle;
    @Bind(R.id.edit_img)
    ImageView editImg;

    /**
     * 上次选中的左侧菜单
     */
    private View lastView;
    /**
     * 常用按钮菜单
     */
    private View oftenView;

    MenuAdapter menuAdapter;
    DeviceAdapter deviceAdapter;

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
        view.findViewById(R.id.often_layout).setOnClickListener(this);
        oftenView = view.findViewById(R.id.often_layout);
        oftenView.setSelected(true);

        menuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long l) {
                if (lastView != null) {
                    //上次选中的view变回灰色
                    lastView.setSelected(false);
                }
                if (oftenView != null) {
                    oftenView.setSelected(false);
                }
                deviceTitle.setText("别克分析仪");
                editImg.setVisibility(View.VISIBLE);
                //设置选中颜色为白色
                view.setSelected(true);
                lastView = view;
            }
        });
    }

    @Override
    protected void initData() {
        menuAdapter = new MenuAdapter(getActivity());
        menuListview.setAdapter(menuAdapter);
        deviceAdapter = new DeviceAdapter(getActivity());
        deviceGridview.setAdapter(deviceAdapter);

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
            case R.id.often_layout:
                oftenView = view;
                deviceTitle.setText("常用设备");
                editImg.setVisibility(View.INVISIBLE);
                view.setSelected(true);
                if (lastView != null) {
                    lastView.setSelected(false);
                }
                break;
            default:
                break;
        }
    }
}
