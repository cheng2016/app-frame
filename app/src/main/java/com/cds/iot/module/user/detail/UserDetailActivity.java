package com.cds.iot.module.user.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserDetailActivity extends BaseActivity implements View.OnClickListener {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    private TextView birthdayTv, sexTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.birthday_layout).setOnClickListener(this);
        findViewById(R.id.sex_layout).setOnClickListener(this);
        birthdayTv = (TextView) findViewById(R.id.birthday_tv);
        sexTv = (TextView) findViewById(R.id.sex_tv);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.birthday_layout:
                String dateStr = birthdayTv.getText().toString();
                Calendar calendar = Calendar.getInstance();
                try {
                    Date date = format.parse(dateStr);
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(UserDetailActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        birthdayTv.setText(format.format(date));
                        Toast.makeText(UserDetailActivity.this, format.format(date), Toast.LENGTH_SHORT).show();
                    }
                })
                .setDate(calendar)
                .setSubmitText("确定")
                .setCancelText("取消")
                .build();
                pvTime.show();
                break;
            case R.id.sex_layout:
                final List optionList = new ArrayList();
                optionList.add("男");
                optionList.add("女");
                int selectid = "男".equals(sexTv.getText().toString())?0:1;
                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(UserDetailActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = (String) optionList.get(options1);
                        sexTv.setText(tx);
                    }
                })
                .setSelectOptions(selectid)  //设置默认选中项
                .setSubmitText("确定")
                .setCancelText("取消")
                .build();
                pvOptions.setPicker(optionList);
                pvOptions.show();
                break;
        }
    }
}
