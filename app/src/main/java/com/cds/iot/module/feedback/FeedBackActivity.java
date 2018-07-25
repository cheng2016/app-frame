package com.cds.iot.module.feedback;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;
import com.cds.iot.util.Logger;
import com.cds.iot.util.ToastUtils;
import com.cds.iot.view.ActionSheetDialog;
import com.cds.iot.view.ActionSheetDialog.*;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    // 拍照回传码
    public final static int PHOTO_REQUEST_CAMERA = 100;
    // 相册选择回传吗
    public final static int PHOTO_REQUEST_GALLERY = 200;


    ImageView feedbackImg;

    private String mFilePath;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        feedbackImg = (ImageView) findViewById(R.id.feedback_img);
        feedbackImg.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        ((TextView)findViewById(R.id.title)).setText("意见反馈");
        //第二个参数是需要申请的权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PHOTO_REQUEST_CAMERA);

        }

        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "temp.png";// 指定路径
    }

    //不指定相机拍摄照片保存地址
    private void takeDefaultPhoto() {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, 1);
    }

    // 指定相机拍摄照片保存地址
    private void takePhoto() {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File file = new File(mFilePath);
        if (file.exists()) {
            file.delete();
        }
        // 把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "系统相机拍照完成，resultCode=" + resultCode);

        if (requestCode == 0) {
            File file = new File(mFilePath);
            Uri uri = Uri.fromFile(file);
            feedbackImg.setImageURI(uri);
        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            Logger.i(TAG, "默认content地址：" + data.getData());
            feedbackImg.setImageURI(data.getData());
//            Picasso.with(this).load(data.getData()).into(feedbackImg);
        }else if(requestCode == PHOTO_REQUEST_GALLERY){

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.feedback_img:
//                takeDefaultPhoto();

                new ActionSheetDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", SheetItemColor.Blue,
                                new OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        // 判断存储卡是否可以用，可用进行存储
                                        /*if (hasSdcard()) {
                                            File f = new File(headImagPath);
                                            if (f.exists()) {
                                                f.delete();
                                            }
                                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                    Uri.fromFile(new File(headImagPath)));
                                            cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                                            startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                                        } else {
                                            ToastUtils.showShort(FeedBackActivity.this,"未找到SD卡，无法进行存储！");
                                        }*/
                                        takeDefaultPhoto();
                                    }
                                })
                        .addSheetItem("从手机相册选择", SheetItemColor.Blue,
                                new OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                                    }
                                }).show();
                break;
        }
    }
}
