package com.cds.iot.module.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;
import com.cds.iot.util.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @Bind(R.id.vp_horizontal_ntb)
    ViewPager viewPager;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;

    public static final int DEFAULT_INDEX = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                radioGroup.check(position + 1);
                switch (position){
                    case 0:
                        radioGroup.check(R.id.device);
                        break;
                    case 1:
                        radioGroup.check(R.id.message);
                        break;
                    case 2:
                        radioGroup.check(R.id.me);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        radioGroup.check(R.id.device);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Logger.i(TAG,"onCheckedChanged：" + checkedId);
//                viewPager.setCurrentItem(checkedId - 1);
                switch (checkedId){
                    case R.id.device: //微信
                        checkedId = 0;
                        break;
                    case R.id.message: //通讯录
                        checkedId = 1;
                        break;
                    case R.id.me: //发现
                        checkedId = 2;
                        break;
                    default:
                        break;
                }
                viewPager.setCurrentItem(checkedId);
            }
        });

        /*final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.btn_device),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.mipmap.btn_device_current))
                        .title(getString(R.string.device))
//                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.btn_message),
                        Color.parseColor(colors[1]))
                        .selectedIcon(getResources().getDrawable(R.mipmap.btn_message_current))
                        .title(getString(R.string.message))
//                        .badgeTitle("with")
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.btn_mine),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.mipmap.btn_mine_current))
                        .title(getString(R.string.my))
//                        .badgeTitle("with")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ALL);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });*/
    }

    @Override
    protected void initData() {

    }


    //双击返回键 退出
    //----------------------------------------------------------------------------------------------
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            ToastUtils.showShort("再次点击返回键退出");
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
