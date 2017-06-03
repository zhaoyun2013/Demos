package com.zhaoyun.utils.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/5/23.
 */

public class RecordVideo {
    public static final int REQUEST_VIDEO = 11;

    private Activity activity;
    Camera mCamera = null;

    public RecordVideo(Activity activity){
        this.activity = activity;
    }

    public void makeVideo(String videoPath,String imagePath) {
        if (mCamera != null) {
            freeCameraResource();
        }
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (mCamera == null)
            return;

        try {
            mCamera.getParameters();
        }

        catch (RuntimeException re) {
            re.printStackTrace();
            mCamera = null;
            showTip();
            return;
        }
        freeCameraResource();
        Intent intent = new Intent(activity, PickVideoActivity.class);
        intent.putExtra(PickVideoActivity.PICK_VIDEO_PATH,videoPath);
        intent.putExtra(PickVideoActivity.PICK_IMAGE_PATH,imagePath);
        activity.startActivityForResult(intent, REQUEST_VIDEO);
    }

    public void play(String videoPath){
        if(!TextUtils.isEmpty(videoPath)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://" + videoPath);
            intent.setData(uri);
            activity.startActivity(intent);
        }
    }

    private void freeCameraResource() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
            }
        }
    }

    private void showTip() {
        //摄像头功能被禁用，请手动打开设置
        new AlertDialog.Builder(activity)
                .setTitle("摄像头功能被禁用，请手动打开设置")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create().show();
    }
}
