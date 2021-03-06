package com.cds.iot.module.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cds.iot.R;
import com.cds.iot.data.entity.Scenes;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {
    private Context context;
    private List<Scenes> mList = new ArrayList<>();

    public DeviceAdapter(Context context) {
        this.context = context;
    }

    public DeviceAdapter(Context context, List<Scenes> list) {
        this.context = context;
        this.mList = list;
    }

    public void updateData(List<Scenes> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

/*    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }*/

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_device, null, false);
            holder.deviceNameTv = (TextView) convertView.findViewById(R.id.device_name);
            holder.deviceStatusTv = (TextView) convertView.findViewById(R.id.device_status);
            holder.deviceImg = (ImageView) convertView.findViewById(R.id.device_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        for (int i = 0; i < 4; i++) {
            holder.deviceNameTv.setText("别克车分析仪");
            holder.deviceStatusTv.setText("离线");
            holder.deviceStatusTv.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.icn_offline),null,null,null);
        }

        return convertView;
    }

    class ViewHolder {
        TextView deviceNameTv, deviceStatusTv;
        ImageView deviceImg;
    }
}
