package com.cds.iot.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cds.iot.util.Logger;

import butterknife.ButterKnife;

/**
 * Created by chengzj on 2017/6/17.
 */

public abstract class BaseFragment extends Fragment{
    public String TAG = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG = this.getClass().getSimpleName();
        View view = inflater.inflate(getLayoutId(),container,false);
        ButterKnife.bind(this, view);
        Logger.d(TAG,"onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.d(TAG,"onViewCreated");
        initView(view,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.d(TAG,"onActivityCreated");
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d(TAG,"onDestroyView");
        ButterKnife.unbind(this);
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract void initData();
}
