package com.cds.iot.module.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cds.iot.module.device.DeviceFragment;
import com.cds.iot.module.message.MessageFragment;
import com.cds.iot.module.user.UserFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] mFragments;


    public MainPagerAdapter(FragmentManager fm, Fragment[] mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DeviceFragment.newInstance();
            case 1:
                return MessageFragment.newInstance();
            case 2:
                return UserFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}
