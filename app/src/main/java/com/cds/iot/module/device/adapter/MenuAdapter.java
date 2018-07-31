package com.cds.iot.module.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cds.iot.R;
import com.cds.iot.data.entity.ScenesDevice;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends BaseAdapter {
    private Context context;

    private List<ScenesDevice> mList = new ArrayList<>();

    public MenuAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<ScenesDevice> list) {
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

    ViewHolder holder;

    @Override
    public View getView(int index, View convertView, ViewGroup viewGroup) {
        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_device_menu, null, false);
            holder.nameTv = (TextView) convertView.findViewById(R.id.menu_name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        for (int i = 0; i < 3; i++) {
            holder.nameTv.setText("别克车分析仪");
            holder.nameTv.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.menu_car_selector),null,null);
        }

        return convertView;
    }

    class ViewHolder {
        TextView nameTv;
        ImageView menuImg;
    }
}
